#!/bin/sh
alias fig='docker run -e http_proxy -v $(pwd):/app -v /var/run/docker.sock:/var/run/docker.sock -ti dduportal/fig'

fig up
