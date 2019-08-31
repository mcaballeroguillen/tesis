#!/bin/bash

java -jar -Xms15G contador.jar /home/mcaballero/pruebas/modelos_autos/interseption3_4/union  /home/mcaballero/pruebas/modelos_autos/interseption_4/union  /home/mcaballero/pruebas/modelos_autos/interseption3_4 &> salida_4

java -jar -Xms15G contador.jar /home/mcaballero/pruebas/modelos_autos/interseption3_8/union  /home/mcaballero/pruebas/modelos_autos/interseption_8/union  /home/mcaballero/pruebas/modelos_autos/interseption3_8 &> salida_8

java -jar -Xms15G contador.jar /home/mcaballero/pruebas/modelos_autos/interseption3_16/union  /home/mcaballero/pruebas/modelos_autos/interseption_16/union  /home/mcaballero/pruebas/modelos_autos/interseption3_16 &> salida_16

java -jar -Xms15G contador.jar /home/mcaballero/pruebas/modelos_autos/interseption3_32/union  /home/mcaballero/pruebas/modelos_autos/interseption_32/union  /home/mcaballero/pruebas/modelos_autos/interseption3_32 &> salida_32

java -jar -Xms15G contador.jar /home/mcaballero/pruebas/modelos_autos/interseption3_64/union  /home/mcaballero/pruebas/modelos_autos/interseption_64/union  /home/mcaballero/pruebas/modelos_autos/interseption3_64 &> salida_64

java -jar -Xms15G contador.jar /home/mcaballero/pruebas/modelos_autos/interseption3_128/union  /home/mcaballero/pruebas/modelos_autos/interseption_128/union  /home/mcaballero/pruebas/modelos_autos/interseption3_128 &> salida_128

java -jar -Xms15G contador.jar /home/mcaballero/pruebas/modelos_autos/interseption3_256/union  /home/mcaballero/pruebas/modelos_autos/interseption_256/union  /home/mcaballero/pruebas/modelos_autos/interseption3_256 &> salida_256

java -jar -Xms15G contador.jar /home/mcaballero/pruebas/modelos_autos/interseption3_500/union  /home/mcaballero/pruebas/modelos_autos/interseption_500/union  /home/mcaballero/pruebas/modelos_autos/interseption3_500 &> salida_500
