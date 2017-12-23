#!/bin/bash

compile () {
    cd results;
    for folder in $(echo */);
    do
        cd $folder;
        for subfolder1 in $(echo */);
        do
            cd $subfolder1
            for subfolder in $(echo */);
            do
                cd $subfolder;
                cd dot
                for dotFile in $(ls *.dot);
                do
                    dot -Tpdf $dotFile > ../pdf/$(basename $dotFile .dot).pdf
                done
                cd ..
                cd stats
                for txtFile in $(ls *.stat *.ap *.as *.rel 2>/dev/null);
                do
                    paps --font="Monospace 6" $txtFile > $txtFile.ps
                    ps2pdf $txtFile.ps ../pdf/$txtFile.pdf
                    rm $txtFile.ps
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
