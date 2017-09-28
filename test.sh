#!/bin/bash
echo -e "\n========================================================================"
echo -e "Without headers..."
echo -e "------------------------------------------------------------------------\n"
curl -H "Origin: http://www.some-domain.com" http://localhost:9000/api/hello/alice -v 
 
echo -e "\n========================================================================"
echo -e "With headers..."
echo -e "------------------------------------------------------------------------\n"
curl -H "Origin: http://www.some-domain.com" -H "Authorization: bob" http://localhost:9000/api/hello/alice  -v

