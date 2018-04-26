@echo off
pushd "%CD%"
CD /D "%~dp0"

color 0f
title Iniciando...

rem inicia o sistema
start backups.jar
exit