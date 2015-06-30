This is a quick project to solve challenge in http://geeks.redmart.com/2015/01/07/skiing-in-singapore-a-coding-diversion/.

I'm not an algorithm expert and I don't remember when was the last time I wrote algorithm in TopCoder contest.

Anyway, this is pretty basic and I just did it for fun.

There are two tests to verify the logic:

* Test using example in the challenge page
* Test using real data. Please check the challenge page for more detail. The expected values might not be correct though.

Technology used in this project

* Language      : Groovy
* Test framework: Spock
* Build tools   : Gradle

I'm using IntelliJ IDEA (and JDK8) but I believe you can run it from console or other IDE as long as you have setup all the necessary tools.

e.g. (in windows cmd) 
-> gradlew.bat cleanTest test
open build/reports/tests/index.html to see the testing
