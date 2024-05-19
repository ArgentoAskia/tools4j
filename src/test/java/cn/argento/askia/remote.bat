@echo off
cls
@echo **********************************
@echo *     请输入要连接的IP并回车     *
@echo **********************************
set /p id=
goto connect

:connect
start mstsc /v:%id%
exit