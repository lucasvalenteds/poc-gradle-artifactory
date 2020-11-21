ARTIFACTORY_URL = http://localhost:8081/artifactory

ARTIFACTORY_REPOSITORY = example-repo-local

ARTIFACTORY_USERNAME = admin

ARTIFACTORY_PASSWORD = password123

JAR_NAME = application

JAR_VERSION = 0.1.0

JAR_FILE = $(JAR_NAME)-$(JAR_VERSION).jar


deploy:
	@./gradlew clean shadowJar artifactoryPublish \
			-Partifactory.url=$(ARTIFACTORY_URL) \
			-Partifactory.repository=$(ARTIFACTORY_REPOSITORY) \
			-Partifactory.username=$(ARTIFACTORY_USERNAME) \
			-Partifactory.password=$(ARTIFACTORY_PASSWORD)

test:
	@curl --silent \
			--user $(ARTIFACTORY_USERNAME):$(ARTIFACTORY_PASSWORD) \
			--output $(JAR_FILE) \
			$(ARTIFACTORY_URL)/$(ARTIFACTORY_REPOSITORY)/com/example/$(JAR_NAME)/$(JAR_VERSION)/$(JAR_FILE)
	@java -jar $(JAR_FILE)
	@rm $(JAR_FILE)
