# TalkAndPlay

## About
Talk and Play is a system that allows people with disabilities to:

- communicate with their environment
- be more independent during their leisure time
- receive training and exercise for rehabilitation at home, under the guidance of their therapist.

This way, they will be able to communicate, listen to music, watch movies and train for a quicker rehabilitation.

## Building and running the project

This project uses [Maven](https://maven.apache.org/) as a build tool.

In order to build the executable files, run:
```bash
mvn clean package
```

This will generate a `target/talkandplay-1.0-SNAPSHOT-jar-with-dependencies.jar` file.

In order to run the file, the **active working directory** must have the following configuration files, as well as the `resources` directory:

```bash
resources/
conf.xml
defaultGames.xml
defaultUser.xml
log4j.properties
properties.xml
sentry.properties
```

## Updater

Once running, the app looks at the URL defined in the `versionFileUrl` field in `properties.xml` in order to check for an update version.
If a new version is found, then the file defined in `zipUrl` is downloaded, extracted, and it's contents overwrite the resource files of the project.

## Development
The project is being developed by SciFY with the exclusive donation from [Stavros Niarchos Foundation](http://www.snf.org/en/).

Upload icon made by [Yannick](http://www.flaticon.com/authors/yannick) from [Flaticon](www.flaticon.com)
