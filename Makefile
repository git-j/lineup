EXECPATH=PATH=/data/scripts/:/opt/android-sdk-update-manager/tools:/opt/android-sdk-update-manager/platform-tools:/usr/games/bin
all:
#	make version
	ant release
#	make sign
	make install
debug:
	ant debug
	make installus
clean:
	ant clean
install:
	adb install -r bin/lineup-release.apk
installus:
	adb install -r bin/lineup-unsigned.apk
environment:
	con 'bash -login'
emulator:
	echo 'make environment? this cannot run from rde - sorry'
	CON_GEOM="20x20+10+10" con "emulator -avd test"
	sleep 1 && CON_GEOM="200x100+410+30" con "adb logcat"
sign:
	rm bin/lineup.apk || echo 'not build yet'
	jarsigner -verbose -keystore apk-release-key.keystore bin/lineup-release-unsigned.apk apk-release-key
	zipalign -v 4 bin/lineup-release-unsigned.apk bin/lineup-release.apk
key:
	keytool -genkey -v -keystore apk-release-key.keystore -alias apk-release-key -keyalg RSA -keysize 2048 -validity 10000
