cd ./fabric
powershell -Command "./gradlew githubRelease";
powershell -Command "./gradlew curseforge";
powershell -Command "./gradlew modrinth";
cd ../forge
powershell -Command "./gradlew githubRelease";
powershell -Command "./gradlew curseforge";
powershell -Command "./gradlew modrinth";
