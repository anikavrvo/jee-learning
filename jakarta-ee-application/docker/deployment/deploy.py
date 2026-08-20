from java.io import FileInputStream
from java.util import Properties

properties = Properties()
properties.load(FileInputStream('/u01/domain.properties'))

username = properties.getProperty('username')
password = properties.getProperty('password')

war = '/u01/deployment/hospital-auths-webapp.war'
app_name = 'hospital-auths-webapp'
target = 'HospitalAuthAdminServer'

print('Connecting to WebLogic...')

connect(username, password, 't3s://weblogic:9002')

print('Connected to WebLogic.')

try:
    print('Checking whether ' + app_name + ' is already deployed...')

    domainConfig()

    app = getMBean(
        'AppDeployments/' + app_name
    )

    if app is not None:
        print('Application already exists.')
        print('Redeploying ' + app_name + '...')

        redeploy(app_name)

        print('Redeployment successful.')

    else:
        print('Application does not exist.')
        print('Deploying ' + app_name + '...')

        deploy(
            app_name,
            war,
            targets=target
        )

        print('Deployment successful.')

finally:
    disconnect()

exit()