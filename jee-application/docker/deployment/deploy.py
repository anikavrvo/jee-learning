from java.io import FileInputStream
from java.util import Properties


# =========================================================
# Utility functions
# =========================================================

def load_properties(path):
    properties = Properties()
    properties.load(FileInputStream(path))
    return properties


# =========================================================
# Configure PostgreSQL DataSource
# =========================================================

# =========================================================
# Configure PostgreSQL DataSource
# =========================================================

def configure_data_source():

    db_properties = load_properties('/u01/database.properties')

    db_host = db_properties.getProperty('db.host')
    db_port = db_properties.getProperty('db.port')
    db_name = db_properties.getProperty('db.name')
    db_username = db_properties.getProperty('db.username')
    db_password = db_properties.getProperty('db.password')

    data_source_name = 'HospitalAuthsDataSource'
    target_name = 'HospitalAuthAdminServer'

    jdbc_url = 'jdbc:postgresql://{}:{}/{}'.format(
        db_host,
        db_port,
        db_name
    )

    print('Configuring ' + data_source_name + '...')
    print('JDBC URL: ' + jdbc_url)

    # -----------------------------------------------------
    # Start edit session
    # -----------------------------------------------------

    edit()
    startEdit()

    try:

        # -------------------------------------------------
        # Get the Domain MBean
        # -------------------------------------------------

        domain = getMBean('/')

        # -------------------------------------------------
        # Check whether the JDBC System Resource already
        # exists INSIDE the edit session.
        # -------------------------------------------------

        jdbc_system_resource = getMBean(
            '/JDBCSystemResources/' + data_source_name
        )

        if jdbc_system_resource is not None:

            print(
                'DataSource already exists. '
                'Using existing JDBCSystemResource.'
            )

        else:

            print(
                'DataSource does not exist. '
                'Creating JDBCSystemResource...'
            )

            jdbc_system_resource = (
                domain.createJDBCSystemResource(
                    data_source_name
                )
            )

            print('JDBCSystemResource created.')

        # -------------------------------------------------
        # Get the JDBC Resource
        #
        # WebLogic automatically creates the JDBCResource
        # descriptor bean underneath the system resource.
        # -------------------------------------------------

        jdbc_resource = jdbc_system_resource.getJDBCResource()

        jdbc_resource.setName(data_source_name)

        # -------------------------------------------------
        # Configure JDBC Driver Parameters
        # -------------------------------------------------

        print('Configuring JDBC driver...')

        driver_params = jdbc_resource.getJDBCDriverParams()

        driver_params.setDriverName(
            'org.postgresql.Driver'
        )

        driver_params.setUrl(
            jdbc_url
        )

        driver_params.setPassword(
            db_password
        )

        # -------------------------------------------------
        # Configure JDBC Driver Properties
        # -------------------------------------------------

        properties = driver_params.getProperties()

        # Avoid creating the "user" property twice if the
        # datasource already exists.
        user_property = properties.lookupProperty('user')

        if user_property is None:

            print('Creating JDBC user property...')

            properties.createProperty(
                'user',
                db_username
            )

        else:

            print('Updating JDBC user property...')

            user_property.setValue(
                db_username
            )

        # -------------------------------------------------
        # Configure JNDI name
        # -------------------------------------------------

        print('Configuring JNDI name...')

        data_source_params = (
            jdbc_resource.getJDBCDataSourceParams()
        )

        data_source_params.setJNDINames(
            [data_source_name]
        )

        # -------------------------------------------------
        # Configure transaction protocol
        #
        # PostgreSQL datasource using a normal non-XA
        # driver does not need global transactions.
        # -------------------------------------------------

        data_source_params.setGlobalTransactionsProtocol(
            'None'
        )

        # -------------------------------------------------
        # Target DataSource
        # -------------------------------------------------

        print(
            'Targeting DataSource at ' +
            target_name +
            '...'
        )

        target_mbean = getMBean(
            '/Servers/' + target_name
        )

        if target_mbean is None:

            raise Exception(
                'Could not find server: ' +
                target_name
            )

        # Only add the target if it isn't already targeted.

        current_targets = (
            jdbc_system_resource.getTargets()
        )

        already_targeted = False

        for current_target in current_targets:

            if current_target.getName() == target_name:

                already_targeted = True
                break

        if already_targeted:

            print(
                'DataSource is already targeted at ' +
                target_name + '.'
            )

        else:

            print(
                'Adding target ' +
                target_name + '...'
            )

            jdbc_system_resource.addTarget(
                target_mbean
            )

        # -------------------------------------------------
        # Save and activate
        # -------------------------------------------------

        print('Saving DataSource configuration...')

        save()

        print(
            'Activating DataSource configuration...'
        )

        activate(
            block='true'
        )

        print(
            'DataSource configuration activated successfully.'
        )

    except:

        print(
            'Failed to configure DataSource.'
        )

        cancelEdit('y')

        raise


# =========================================================
# Load WebLogic admin credentials
# =========================================================

properties = load_properties(
    '/u01/domain.properties'
)

username = properties.getProperty(
    'username'
)

password = properties.getProperty(
    'password'
)


# =========================================================
# Application configuration
# =========================================================

war = '/u01/deployment/hospital-auths-webapp.war'

app_name = 'hospital-auths-webapp'

target = 'HospitalAuthAdminServer'


# =========================================================
# Connect to WebLogic
# =========================================================

print('Connecting to WebLogic...')

connect(
    username,
    password,
    't3s://weblogic:9002'
)

print('Connected to WebLogic.')


try:

    # =====================================================
    # Configure PostgreSQL DataSource
    # =====================================================

    configure_data_source()


    # =====================================================
    # Deploy / redeploy application
    # =====================================================

    print(
        'Checking whether ' + app_name +
        ' is already deployed...'
    )

    domainConfig()

    app = getMBean(
        '/AppDeployments/' + app_name
    )

    if app is not None:

        print('Application already exists.')

        print(
            'Redeploying ' + app_name + '...'
        )

        redeploy(
            app_name
        )

        print(
            'Redeployment successful.'
        )

    else:

        print(
            'Application does not exist.'
        )

        print(
            'Deploying ' + app_name + '...'
        )

        deploy(
            app_name,
            war,
            targets=target
        )

        print(
            'Deployment successful.'
        )


finally:

    disconnect()


exit()