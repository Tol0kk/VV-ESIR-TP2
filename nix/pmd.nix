{ lib, stdenv, fetchzip, unzip, makeWrapper, openjdk, libGL, xorg, jdk, wrapGAppsHook3 }:

stdenv.mkDerivation rec {
  pname = "pmd";
  version = "7.5.0";

  src = fetchzip {
    url = "https://github.com/pmd/pmd/releases/download/pmd_releases%2F${version}/pmd-dist-${version}-bin.zip";
    hash = "sha256-Kfikut45HtQ+UZTJFZZdNqLd4iMQrvHUb5IyLtGbdPI=";
  };

  patches = [ ./pmd-conf.patch ]; # Remove Config folder Creation in the pmd bash script.

  nativeBuildInputs = [ xorg.libXxf86vm.out libGL xorg.libXtst makeWrapper wrapGAppsHook3 ];

  dontConfigure = true;
  dontBuild = true;

  installPhase = ''
    runHook preInstall

    install -Dm755 bin/pmd $out/libexec/pmd
    install -Dm644 lib/*.jar -t $out/lib/pmd

    wrapProgram $out/libexec/pmd \
        --prefix PATH : ${jdk}/bin --set LIB_DIR $out/lib/pmd

    makeWrapper $out/libexec/pmd $out/bin/pmd-check --argv0 check --add-flags check
    makeWrapper $out/libexec/pmd $out/bin/pmd-designer --argv0 designer --add-flags designer --set LD_LIBRARY_PATH "${xorg.libXxf86vm}/lib:${libGL}/lib:${xorg.libXtst}/lib"
                  
    # for app in pmd cpd cpdgui designer bgastviewer designerold ast-dump; do
    #     makeWrapper $out/libexec/pmd $out/bin/$app --argv0 $app --add-flags $app
    # done

    runHook postInstall
  '';

  meta = with lib; {
    description = "Extensible cross-language static code analyzer";
    homepage = "https://pmd.github.io/";
    changelog = "https://pmd.github.io/pmd-${version}/pmd_release_notes.html";
    platforms = platforms.unix;
    license = with licenses; [ bsdOriginal asl20 ];
  };
}
