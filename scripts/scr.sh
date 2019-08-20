#!/bin/bash

java -jar -Xms15G normal_500.jar /home/mcaballero/datos/wikidata-20190704-truthy-BETA.nt.gz /home/mcaballero/pruebas/peliculas "<http://www.wikidata.org/entity/Q3231690>" 0 &> salida500

java -jar -Xms15G normal_256.jar /home/mcaballero/datos/wikidata-20190704-truthy-BETA.nt.gz /home/mcaballero/pruebas/peliculas "<http://www.wikidata.org/entity/Q3231690>" 0 &> salida256

java -jar -Xms15G normal_128.jar /home/mcaballero/datos/wikidata-20190704-truthy-BETA.nt.gz /home/mcaballero/pruebas/peliculas "<http://www.wikidata.org/entity/Q3231690>" 0 &> salida128

java -jar -Xms15G normal_64.jar /home/mcaballero/datos/wikidata-20190704-truthy-BETA.nt.gz /home/mcaballero/pruebas/peliculas "<http://www.wikidata.org/entity/Q3231690>" 0 &> salida64

java -jar -Xms15G normal_32.jar /home/mcaballero/datos/wikidata-20190704-truthy-BETA.nt.gz /home/mcaballero/pruebas/peliculas "<http://www.wikidata.org/entity/Q3231690>" 0 &> salida32

java -jar -Xms15G normal_16.jar /home/mcaballero/datos/wikidata-20190704-truthy-BETA.nt.gz /home/mcaballero/pruebas/peliculas "<http://www.wikidata.org/entity/Q3231690>" 0 &> salida16

java -jar -Xms15G normal_8.jar /home/mcaballero/datos/wikidata-20190704-truthy-BETA.nt.gz /home/mcaballero/pruebas/peliculas "<http://www.wikidata.org/entity/Q3231690>" 0 &> salida8

java -jar -Xms15G normal_4.jar /home/mcaballero/datos/wikidata-20190704-truthy-BETA.nt.gz /home/mcaballero/pruebas/peliculas "<http://www.wikidata.org/entity/Q3231690>" 0 &> salida4
