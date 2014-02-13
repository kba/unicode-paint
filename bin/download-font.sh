#!/bin/bash
FONTDIR=$PWD/src/main/resources/fonts/

download_unifont() {
    VERSION=6.3.20131221
    wget http://unifoundry.com/pub/unifont-6.3.20131221/font-builds/unifont-${VERSION}.ttf.gz
    gunzip unifont-${VERSION}.ttf.gz
    mv unifont-${VERSION}.ttf $FONTDIR/unifont.ttf
}
download_quivira() {
    wget http://www.quivira-font.com/files/Quivira.otf
    mv Quivira.otf $FONTDIR
}
download_symbola() {
    wget http://users.teilar.gr/~g1951d/Symbola.ttf
    mv Symbola.ttf $FONTDIR
}
download_monaco() {
    wget https://github.com/todylu/monaco.ttf/raw/master/monaco.ttf
    mv monaco.ttf $FONTDIR
}
download_monospace() {
    wget http://www.fontspace.com/download/13274/f5c14068e8b84b83aa5e60a1ccb3195d/george-williams_monospace.zip
}
download_ubuntu() {
    wget http://font.ubuntu.com/download/ubuntu-font-family-0.80.zip
    unzip *zip
    cp ubuntu-font-family-0.80/UbuntuMono-R.ttf $FONTDIR
    cp ubuntu-font-family-0.80/UbuntuMono-B.ttf $FONTDIR
}

if [[ "$#" == "0" ]];then
    echo "USAGE $0 (unifont|quivira)"
fi

TMPDIR=$(mktemp -d)
cd $TMPDIR

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
    elif [[ $1 = "ubuntu" ]];then
        download_ubuntu
    else
        echo "Unknown argument $1"
        exit 1
    fi
    shift
done
echo rm $TMPDIR
# rm -r $TMPDIR
