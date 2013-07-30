$Id: README.txt,v 1.5 2002/05/24 09:23:02 glurk Exp $


Prerequisites:
==============
* JDK >= 1.2
* Recommended: 3-4x the size of the input Jar files as available RAM


How to use JavaGuard
====================
JavaGuard uses Jakarta-ORO (http://jakarta.apache.org) for regular expression
matching. Therefore the classpath must contain the jakarta-oro-2.0.6.jar file
(or a compatible version) which is provided in the current release:

~> java -cp javaguard.jar;jakarta-oro-2.0.6.jar JavaGuard <parameters>

Without any command line parameters a short description is printed to your
console.

JavaGuard requires at least two parameters: one that specifies the input Jar
file that should be obfuscated and another one that specifies the name of the
output Jar file that holds the obfuscated contents.

Additionally a script file can be given to influence the behaviour of the
obfuscator, i.e. to prevent certain classes, fields and methods etc. from being
renamed etc. To allow the user to see what JavaGuard has done a log file can be
created at the end so he/she can check whether everything worked as he/she
expected :-) (A sample script file is provided in the current release).

JavaGuard understands the following parameters:


-i <input-file>  The name of an input JAR file to be obfuscated
--input

-d <dir> <regex> Obfuscate all files below the directory that match the regular
--dir            expression

-o <output-file> The name of the output file that will contain the obfuscated
--output         contents of all input files

-s <script>      The name of a valid JavaGuard script file
--script

-l <log-file>    The name for the log file
--log

--dump           Dump the parsed class tree before obfuscation.

-v               Increment the logging level. To be more verbose specify the
--verbose        parameter several times.

-h               Show this info page.
--help

--version        Show the program version.


Example:
%> java -cp javaguard.jar;jakarta-oro-2.0.6.jar JavaGuard -i input.jar \
     -o output-obfuscated.jar -l logfile.txt -s scriptfile.txt -v -v -v
