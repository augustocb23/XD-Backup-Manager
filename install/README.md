# Script de instalação NSIS

## Executável `.exe`

 Como Java exporta um arquivo com a extensão `.jar`, um arquivo `.bat` 
inicia o programa e garante que os ícones dos atalhos (Menu Iniciar e Área de Trabalho) sejam definidos corretamente. 

Este arquivo e o ícone no formato `.ico` estão na pasta [bat](bat)

## Script `.nsi`

O script precisa ser compilado a partir da ferramenta [NSIS](http://nsis.sourceforge.net/Download).

Os arquivos do programa serão compilados - `backups.exe`, `backups.png` e `backups.jar` - e será gerado um instalador 
que criará os 
atalhos e 
informará caso o 
Java 
não seja encontrado. O instalador também exibe o arquivo `license.txt`.