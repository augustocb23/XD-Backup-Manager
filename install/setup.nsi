; Script generated by the HM NIS Edit Script Wizard.

; HM NIS Edit Wizard helper defines
!define PRODUCT_NAME "XD Backup Manager"
!define PRODUCT_VERSION "1.0"
!define PRODUCT_PUBLISHER "Augusto C�sar Bisognin"
!define PRODUCT_WEB_SITE "https://github.com/augustocb23"
!define PRODUCT_DIR_REGKEY "Software\Microsoft\Windows\CurrentVersion\App Paths\backups.exe"
!define PRODUCT_UNINST_KEY "Software\Microsoft\Windows\CurrentVersion\Uninstall\${PRODUCT_NAME}"
!define PRODUCT_UNINST_ROOT_KEY "HKLM"

SetCompressor lzma

; MUI 1.67 compatible ------
!include "MUI.nsh"

!include LogicLib.nsh

; MUI Settings
!define MUI_ABORTWARNING
!define MUI_ICON "${NSISDIR}\Contrib\Graphics\Icons\modern-install.ico"
!define MUI_UNICON "${NSISDIR}\Contrib\Graphics\Icons\modern-uninstall.ico"

; Welcome page
!insertmacro MUI_PAGE_WELCOME
; License page
!insertmacro MUI_PAGE_LICENSE "license.txt"
; Directory page
!insertmacro MUI_PAGE_DIRECTORY
; Instfiles page
!insertmacro MUI_PAGE_INSTFILES
; Finish page
!define MUI_FINISHPAGE_RUN "$INSTDIR\backups.exe"
!insertmacro MUI_PAGE_FINISH

; Uninstaller pages
!insertmacro MUI_UNPAGE_INSTFILES

; Language files
!insertmacro MUI_LANGUAGE "PortugueseBR"

; Reserve files
!insertmacro MUI_RESERVEFILE_INSTALLOPTIONS

; MUI end ------

Name "${PRODUCT_NAME} ${PRODUCT_VERSION}"
OutFile "instalar.exe"
InstallDir "$PROGRAMFILES\XD Backup Manager"
InstallDirRegKey HKLM "${PRODUCT_DIR_REGKEY}" ""
ShowInstDetails show
ShowUnInstDetails show

Section "Se��oPrincipal" SEC01
  SetOutPath "$INSTDIR"
  SetOverwrite ifnewer
  File "backups.png"
  File "backups.jar"
  File "backups.exe"
  CreateDirectory "$SMPROGRAMS\XD Backup Manager"
  CreateShortCut "$SMPROGRAMS\XD Backup Manager\XD Backup Manager.lnk" "$INSTDIR\backups.exe"
  CreateShortCut "$DESKTOP\XD Backup Manager.lnk" "$INSTDIR\backups.exe"
SectionEnd

Section -AdditionalIcons
  WriteIniStr "$INSTDIR\${PRODUCT_NAME}.url" "InternetShortcut" "URL" "${PRODUCT_WEB_SITE}"
  CreateShortCut "$SMPROGRAMS\XD Backup Manager\Website.lnk" "$INSTDIR\${PRODUCT_NAME}.url"
  CreateShortCut "$SMPROGRAMS\XD Backup Manager\Uninstall.lnk" "$INSTDIR\uninst.exe"
SectionEnd

Section -Post
  WriteUninstaller "$INSTDIR\uninst.exe"
  WriteRegStr HKLM "${PRODUCT_DIR_REGKEY}" "" "$INSTDIR\backups.exe"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "DisplayName" "$(^Name)"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "UninstallString" "$INSTDIR\uninst.exe"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "DisplayIcon" "$INSTDIR\backups.exe"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "DisplayVersion" "${PRODUCT_VERSION}"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "URLInfoAbout" "${PRODUCT_WEB_SITE}"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "Publisher" "${PRODUCT_PUBLISHER}"
SectionEnd

; verifica se o Java 8 est� instalado
Section "Java"
  SetRegView 64
    ReadRegStr $0 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" CurrentVersion
  SetRegView 32
    ReadRegStr $1 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" CurrentVersion

  ${If} $0 < "1.8"
    ${If} $1 < "1.8"
      MessageBox MB_OK "JRE n�o instalado ou desatualizado. Fa�a o download em java.com antes de iniciar o programa"
      ${EndIf}
  ${EndIf}
SectionEnd

Function un.onUninstSuccess
  HideWindow
  MessageBox MB_ICONINFORMATION|MB_OK "$(^Name) foi removido com sucesso do seu computador."
FunctionEnd

Function un.onInit
  MessageBox MB_ICONQUESTION|MB_YESNO|MB_DEFBUTTON2 "Tem certeza que quer remover completamente $(^Name) e todos os seus componentes? Dados do usu�rio n�o ser�o apagados." IDYES +2
  Abort
FunctionEnd

Section Uninstall
  Delete "$INSTDIR\${PRODUCT_NAME}.url"
  Delete "$INSTDIR\uninst.exe"
  Delete "$INSTDIR\backups.exe"
  Delete "$INSTDIR\backups.jar"
  Delete "$INSTDIR\backups.png"
  Delete "$INSTDIR\backups.sqlite"

  Delete "$SMPROGRAMS\XD Backup Manager\Uninstall.lnk"
  Delete "$SMPROGRAMS\XD Backup Manager\Website.lnk"
  Delete "$DESKTOP\XD Backup Manager.lnk"
  Delete "$SMPROGRAMS\XD Backup Manager\XD Backup Manager.lnk"

  RMDir "$SMPROGRAMS\XD Backup Manager"
  RMDir "$INSTDIR"

  DeleteRegKey ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}"
  DeleteRegKey HKLM "${PRODUCT_DIR_REGKEY}"
  SetAutoClose true
SectionEnd