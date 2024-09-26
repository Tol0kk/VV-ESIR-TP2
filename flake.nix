{
  description = "A Nix-flake-based Java development environment";

  inputs.nixpkgs.url = "github:nixos/nixpkgs/nixos-24.05";

  outputs = { self, nixpkgs }:
    let
      javaVersion = 21; # Change this value to update the whole stack

      supportedSystems = [ "x86_64-linux" "aarch64-linux" "x86_64-darwin" "aarch64-darwin" ];
      forEachSupportedSystem = f: nixpkgs.lib.genAttrs supportedSystems (system: f {
        pkgs = import nixpkgs { inherit system; overlays = [ self.overlays.default ]; };
      });
    in
    {
      overlays.default =
        final: prev: rec {
          jdk = prev."jdk${toString javaVersion}".override { enableJavaFX = false; };
          maven = prev.maven.override { inherit jdk; };
          gradle = prev.gradle.override { java = jdk; };
          pmd = prev.callPackage ./nix/pmd.nix { inherit jdk; };
          openjfx = prev.callPackage ./nix/openjfx.nix { inherit jdk; };
        };

      devShells = forEachSupportedSystem ({ pkgs }: {
        default = pkgs.mkShell {
          JAVAFX_HOME = "${pkgs.openjfx}";
          packages = with pkgs; [
            gcc
            gradle
            jdk
            maven
            ncurses
            patchelf
            pmd
          ];
        };
      });
    };
}