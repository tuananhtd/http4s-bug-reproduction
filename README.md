# http4s-bug-reproduction
Clone the project

`cd` to the folder

To create the response that causes the error: `cat fail | nc -l 8000`

To create the similar response when parts is reversed and will not cause the error: `cat success | nc -l 8000`

start sbt with `sbt` command and then run the project with `run` to send the request.
