set mods=autoadvance camera core gearex lootfilter noloading nonews pandora pocketshop showping stayonline teleport tweaks

md target\code-mods\

for %%i in (%mods%) do copy "%cd%\%%i\target\*.jar" "%cd%\target\code-mods\"

