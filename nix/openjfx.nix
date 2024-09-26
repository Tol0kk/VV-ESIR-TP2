{ lib, stdenv, fetchzip, jdk }:

stdenv.mkDerivation rec {
  pname = "openjfx";
  version = "21.0.4";

  src = fetchzip {
    url = "https://download2.gluonhq.com/openjfx/${version}/openjfx-${version}_linux-x64_bin-sdk.zip";
    sha256 = "sha256-FDmnv72ldE5xC83zG4VBxKYrpXE2/7P6JR1iRp76eVw=";
  };

  installPhase = ''
    mkdir $out
    cp -r * $out/.
  '';

  meta = with lib; {
    description = "OpenJFX is an open-source, next-generation client application platform for desktop, mobile, and embedded systems built on Java.";
    homepage = "https://openjfx.io/";
    license = licenses.gpl2;
    platforms = platforms.unix;
  };
}