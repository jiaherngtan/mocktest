# mocktest

List of commands:
mvn compile exec:java -Dexec.mainClass="vtp2022.dayx.mocktest.Main"
mvn compile exec:java -Dexec.mainClass="vtp2022.dayx.mocktest.Main" -Dexec.args="--port 8080"
mvn compile exec:java -Dexec.mainClass="vtp2022.dayx.mocktest.Main" -Dexec.args="--docRoot ./target:opt/tmp/www"
mvn compile exec:java -Dexec.mainClass="vtp2022.dayx.mocktest.Main" -Dexec.args="--port 8080 --docRoot ./target:opt/tmp/www"