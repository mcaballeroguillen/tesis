#!/bin/bash

java -jar -Xms15G proba_500.jar /home/mcaballero/datos/wikidata-20190704-truthy-BETA.nt.gz /home/mcaballero/pruebas/paises "<http://www.wikidata.org/entity/Q3231690>" 0 &> salida500

java -jar -Xms15G proba_256.jar /home/mcaballero/datos/wikidata-20190704-truthy-BETA.nt.gz /home/mcaballero/pruebas/paises "<http://www.wikidata.org/entity/Q3231690>" 0 &> salida256

java -jar -Xms15G proba_128.jar /home/mcaballero/datos/wikidata-20190704-truthy-BETA.nt.gz /home/mcaballero/pruebas/paises "<http://www.wikidata.org/entity/Q3231690>" 0 &> salida128

java -jar -Xms15G proba_64.jar /home/mcaballero/datos/wikidata-20190704-truthy-BETA.nt.gz /home/mcaballero/pruebas/paises "<http://www.wikidata.org/entity/Q3231690>" 0 &> salida64

java -jar -Xms15G proba_32.jar /home/mcaballero/datos/wikidata-20190704-truthy-BETA.nt.gz /home/mcaballero/pruebas/paises "<http://www.wikidata.org/entity/Q3231690>" 0 &> salida32

java -jar -Xms15G proba_16.jar /home/mcaballero/datos/wikidata-20190704-truthy-BETA.nt.gz /home/mcaballero/pruebas/paises "<http://www.wikidata.org/entity/Q3231690>" 0 &> salida16

java -jar -Xms15G proba_8.jar /home/mcaballero/datos/wikidata-20190704-truthy-BETA.nt.gz /home/mcaballero/pruebas/paises "<http://www.wikidata.org/entity/Q3231690>" 0 &> salida8

java -jar -Xms15G proba_4.jar /home/mcaballero/datos/wikidata-20190704-truthy-BETA.nt.gz /home/mcaballero/pruebas/paises "<http://www.wikidata.org/entity/Q3231690>" 0 &> salida4
