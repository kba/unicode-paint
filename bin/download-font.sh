#!/bin/bash
download_unifont() {
    VERSION=6.3.20131221
    wget http://unifoundry.com/pub/unifont-6.3.20131221/font-builds/unifont-${VERSION}.ttf.gz
    gunzip unifont-${VERSION}.ttf.gz
    mv unifont-${VERSION}.ttf src/main/resources/unifont.ttf
}
download_quivira() {
    wget http://www.quivira-font.com/files/Quivira.otf
    mv Quivira.otf src/main/resources
}
download_symbola() {
    wget http://users.teilar.gr/~g1951d/Symbola.ttf
    mv Symbola.ttf src/main/resources
}
download_monaco() {
    wget https://github.com/todylu/monaco.ttf/raw/master/monaco.ttf
    mv monaco.ttf src/main/resources
}
download_monospace() {
    wget http://www.fontspace.com/download/13274/f5c14068e8b84b83aa5e60a1ccb3195d/george-williams_monospace.zip
}

if [[ "$#" == "0" ]];then
    echo "USAGE $0 (unifont|quivira)"
fi
while (( "$#" ));do
    if [[ $1 = "unifont" ]];then
        download_unifont
    elif [[ $1 = "quivira" ]];then
        download_quivira
    elif [[ $1 = "symbola" ]];then
        download_symbola
    elif [[ $1 = "monaco" ]];then
        download_monaco
    elif [[ $1 = "monospace" ]];then
        download_monospace
    else
        echo "Unknown argument $1"
        exit 1
    fi
    shift
done
