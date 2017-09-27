#!/bin/bash

curl -H "Origin: http://www.some-domain.com" http://localhost:9000/api/hello/123  -v

curl -H "Origin: http://www.some-domain.com" -H "Authorization: me" http://localhost:9000/api/hello/123  -v
