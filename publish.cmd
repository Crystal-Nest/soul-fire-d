cd ./fabric
powershell -Command "./gradlew githubRelease";
powershell -Command "./gradlew curseforge";
cd ../forge
powershell -Command "./gradlew githubRelease";
powershell -Command "./gradlew curseforge";