@ECHO OFF
pushd "%~dp0"
if exist tnp*_*.exe (
	move tnp*_*.exe tnp.exe
)
start /b tnp.exe