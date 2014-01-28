#!/bin/bash
VERSION=6.3.20131221
wget http://unifoundry.com/pub/unifont-6.3.20131221/font-builds/unifont-${VERSION}.ttf.gz
gunzip unifont-${VERSION}.ttf.gz
mv unifont-${VERSION}.ttf src/main/resources/unifont.ttf
