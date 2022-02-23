def branchName = "${env.BRANCH_NAME}";
def profile = 'dev';
def ssh = '';
def ssh2 = '';
def dockerTag = '';
def projectName = 'message-server'
if(branchName == 'main') {
    profile = 'pro';
    ssh = 'root@114.55.32.181';
    ssh2 = 'root@114.55.33.62';
    dockerTag = branchName;
} else {
    profile = 'dev';
    ssh = 'root@192.168.166.107';
    dockerTag = branchName;
}

pipeline {
  agent any

  options {
    timeout(time: 5, unit: 'MINUTES')
  }

  triggers {
    GenericTrigger(
        genericVariables: [
        [key: 'ref', value: '$.ref'],
        [key: 'user_name', value: '$.user_name'],
        [key: 'message', value: '$.commits[0].message'],
        ],
        causeString: 'Triggered on $ref by $user_name',
        token: 'message-server-#pji',
        tokenCredentialId: '',
        printContributedVariables: true,
        printPostContent: true,
        silentResponse: false,
        regexpFilterText: "${BRANCH_NAME}" + '$ref$message',
        regexpFilterExpression: '^(trunkrefs/heads/trunk)(.|\n)*(?=ci)'
    )
  }

  stages {
    stage('maven build') {
      steps {
        sh 'mvn clean -U package -Dmaven.test.skip=true --settings /var/jenkins_home/settings.xml'
      }
    }

    stage('create docker context') {
      steps {
        sh """
        rm -rf docker && mkdir docker && \\
        cp message-server/target/${projectName}-1.0.0-SNAPSHOT.jar docker/${projectName}-1.0.0-SNAPSHOT.jar && \\
        cp Dockerfile docker/Dockerfile
        """
      }
    }

    stage('docker build') {
      steps {
        dir(path: 'docker') {
          sh "docker build -t registry.cn-hangzhou.aliyuncs.com/jszn/home-school:${projectName}-${dockerTag} ."
        }
      }
    }

    stage('docker push') {
      steps {
        sh "docker push registry.cn-hangzhou.aliyuncs.com/jszn/home-school:${projectName}-${dockerTag}"
      }
    }

    stage('delete docker image') {
      steps {
        sh "docker rmi registry.cn-hangzhou.aliyuncs.com/jszn/home-school:${projectName}-${dockerTag}"
      }
    }

    stage('deploy') {
      when {
        branch 'trunk'
      }
      steps {
        retry(count: 3) {
          sshagent(credentials: ['ssh']) {
            sh """
            ssh -o StrictHostKeyChecking=no "${ssh}" "docker stop ${projectName} || true && docker rm ${projectName} || true && \\
            docker rmi registry.cn-hangzhou.aliyuncs.com/jszn/home-school:${projectName}-${dockerTag} || true && \\
            docker run -itd --init -p 9040:9040 --name ${projectName} --restart always --log-opt max-size=100m --log-opt max-file=3 -v /opt/log/tuoyang/hs/message/:/opt/log/tuoyang/hs/ -e TZ="Asia/Shanghai" -e SPRING.PROFILES.ACTIVE=${profile} registry.cn-hangzhou.aliyuncs.com/jszn/home-school:${projectName}-${dockerTag}"
            """
          }
        }
      }
    }

    stage('deploy on redis') {
      when {
        branch 'main'
      }
      steps {
        retry(count: 3) {
          sshagent(credentials: ['ssh']) {
            sh """
            ssh -o StrictHostKeyChecking=no "${ssh}" "docker stop ${projectName} || true && docker rm ${projectName} || true && \\
            docker rmi registry.cn-hangzhou.aliyuncs.com/jszn/home-school:${projectName}-${dockerTag} || true && \\
            docker run -itd --init -p 9040:9040 --name ${projectName} --restart always --log-opt max-size=100m --log-opt max-file=3 -v /opt/log/tuoyang/hs/message/:/opt/log/tuoyang/hs/ -e TZ="Asia/Shanghai" -e SPRING.PROFILES.ACTIVE=${profile} registry.cn-hangzhou.aliyuncs.com/jszn/home-school:${projectName}-${dockerTag}"
            """
          }
        }
      }
    }

    stage('deploy on web') {
      when {
        branch 'main'
      }
      steps {
        retry(count: 3) {
          sshagent(credentials: ['ssh']) {
            sh """
            ssh -o StrictHostKeyChecking=no "${ssh2}" "docker stop ${projectName} || true && docker rm ${projectName} || true && \\
            docker rmi registry.cn-hangzhou.aliyuncs.com/jszn/home-school:${projectName}-${dockerTag} || true && \\
            docker run -itd --init -p 9040:9040 --name ${projectName} --restart always --log-opt max-size=100m --log-opt max-file=3 -v /opt/log/tuoyang/hs/message/:/opt/log/tuoyang/hs/ -e TZ="Asia/Shanghai" -e SPRING.PROFILES.ACTIVE=${profile} registry.cn-hangzhou.aliyuncs.com/jszn/home-school:${projectName}-${dockerTag}"
            """
          }
        }
      }
    }
  }

  post {
    unstable {
       dingtalk (
           robot: '56a5144c-228f-42b5-8ae3-78a7a8bf07fa',
           type: 'ACTION_CARD',
           title: "${JOB_NAME}-${BRANCH_NAME} unstable",
           text: [
           "## [${JOB_NAME}](${BUILD_URL})",
           "---",
           "- 任务：[${BUILD_NUMBER}](${BUILD_URL})",
           "- 状态：\u003cfont color\u003d#f5222d\u003e失败\u003c/font\u003e"
           ],
           btns: [
               [
                  title: '更改记录',
                  actionUrl: "${BUILD_URL}/changes"
               ],
              [
                  title: '控制台',
                  actionUrl: "${BUILD_URL}/console"
              ]
           ],
           btnLayout: 'H',
           hideAvatar: false
       )
    }

    success {
        dingtalk (
            robot: '56a5144c-228f-42b5-8ae3-78a7a8bf07fa',
            type: 'ACTION_CARD',
            title: "${JOB_NAME}-${BRANCH_NAME} 成功",
            text: [
            "## [${JOB_NAME}](${BUILD_URL})",
            "---",
            "- 任务：[${BUILD_NUMBER}](${BUILD_URL})",
            "- 状态：\u003cfont color\u003d#52c41a\u003e成功\u003c/font\u003e"
            ],
            btns: [
                [
                   title: '更改记录',
                   actionUrl: "${BUILD_URL}/changes"
                ],
               [
                   title: '控制台',
                   actionUrl: "${BUILD_URL}/console"
               ]
            ],
            btnLayout: 'H',
            hideAvatar: false
        )
    }

    failure {
        dingtalk (
            robot: '56a5144c-228f-42b5-8ae3-78a7a8bf07fa',
            type: 'ACTION_CARD',
            title: "${JOB_NAME}-${BRANCH_NAME} 失败",
            text: [
            "## [${JOB_NAME}](${BUILD_URL})",
            "---",
            "- 任务：[${BUILD_NUMBER}](${BUILD_URL})",
            "- 状态：\u003cfont color\u003d#f5222d\u003e失败\u003c/font\u003e"
            ],
            btns: [
                [
                   title: '更改记录',
                   actionUrl: "${BUILD_URL}/changes"
                ],
               [
                   title: '控制台',
                   actionUrl: "${BUILD_URL}/console"
               ]
            ],
            btnLayout: 'H',
            hideAvatar: false
        )
    }
  }
}