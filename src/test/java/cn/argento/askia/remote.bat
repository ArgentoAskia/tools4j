@echo off
cls
@echo **********************************
@echo *     ������Ҫ���ӵ�IP���س�     *
@echo **********************************
set /p id=
goto connect

:connect
start mstsc /v:%id%
exit