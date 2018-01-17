#!/bin/bash

compile () {
    cd results;
    for folder in $(echo */);
    do
        cd ${folder};
        for subfolder1 in $(echo */);
        do
            cd ${subfolder1}
            for subfolder in $(echo */);
            do
                cd ${subfolder};
                cd stats
                for rowFile in cxp.row cxpaso.row rcxp.row rcxpaso.row;
                do
                    echo ${folder} ${rowFile} $(cat ${rowFile})
                done
                cd ../../..;
            done
        done
        cd ..;
    done
    cd ..
}

case $# in
    0) 
        compile;;
    1) 
        while true;
        do
            compile;
            sleep 1
        done;;
    *) 
        echo "USAGE: pdfMaker.sh [-l|--loop]";;
esac
