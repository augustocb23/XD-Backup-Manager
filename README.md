# XD Backup Manager
Um gerenciador de backups em Java utilizando um banco de dados em [SQLite](http://sqlite.org/).

## Permite ao usuário criar pacotes, contendo:
- Um nome;
- Um ator (pessoa associada ao pacote), como um cliente, um desenvolvedor, autor, etc;
- Uma data de criação;
- Um tipo;
- Tamanho;
- Uma ou mais tags;

## Um ou mais pacotes são organizados em backups, que por sua vez contém:
- Um código gerado automaticamente (pode ser alterado posteriormente);
- Data de gravação;
- Tamanho total (soma do tamanho dos pacotes, caso tenha sido informado);
- Quantidade de pacotes;

  Cada backup representa, portanto, um conjunto de pacotes que podem ser salvos em discos (DVD, Blu-Ray) anotando apenas seu código (número). Quando necessário, basta consultar o banco de dados para localizar em qual mídia foi gravado um determinado pacote.
  É possível buscar os pacotes por nome, tipo, tags ou atores, ou filtrar por backups.

# Instalação
Baixe e execute o arquivo _instalar.exe_. Caso o Java não tenha tenha sido detectado, será exibida uma mensagem informando que este deve ser instalado.
Ao abrir o programa pela primeira vez, será exibida uma mensagem que um novo banco de dados foi gerado.

# Licença
  O software utiliza o [SQLite JDBC Driver](https://github.com/xerial/sqlite-jdbc), por Xerial.
  Distribuído sob a [Licença Apache 2.0](http://www.apache.org/licenses/).
