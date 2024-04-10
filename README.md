# TP1-Aed3

## Mini Banco de Dados de Violões em arquivos
Este é um projeto onde é feito um mini banco de dados, sem uso de SGBD, onde é armazenado dados com informações sobre violões em 1 arquivo com extensão ".db". Foi implementado um CRUD para pode ser realizado ações no arquivo. Nas ações Create e Uptade foi feito em exclusivo um forma de reaproveitar espaços excluídos ao criar ou atualizar objetos.Este Trabalho Prático aplicado na Disciplina AEDS-3 da Puc Minas, me ajudou a perceber a enorme capacidade de se fazer banco de dados com excelência utilizando nenhum Software próprio pra isso.

### Alunos integrantes da equipe

* Pedro Rodrigues Alves

### Professores responsáveis

* Marcos André Silveira Kutova

### Instruções de utilização

Em um terminal digite "javac Main.java", de enter, depois digite "java Main", de enter.

### CheckList do Projeto

* #### O que você considerou como perda aceitável para o reuso de espaços vazios, isto é, quais são os critérios para a gestão dos espaços vazios?
Caso o objeto novo seja menor ou igual o espaço ocupado pelo objeto antigo, aquele poderá ser reutilizado.

* #### O código do CRUD com arquivos de tipos genéricos está funcionando corretamente?
Sim

* #### O CRUD tem um índice direto implementado com a tabela hash extensível?
Não

* #### A operação de inclusão busca o espaço vazio mais adequado para o novo registro antes de acrescentá-lo ao fim do arquivo?
SIm

* #### A operação de alteração busca o espaço vazio mais adequado para o registro quando ele cresce de tamanho antes de 
acrescentá-lo ao fim do arquivo? Sim

* #### As operações de alteração (quando for o caso) e de exclusão estão gerenciando os espaços vazios para que possam ser reaproveitados? Sim

* #### O trabalho está funcionando corretamente?
Sim

* #### O trabalho está completo?
Sim

* #### O trabalho é original e não a cópia de um trabalho de um colega?
O trabalho não é copia de nenhum colega e foi feito baseado no código do professor responsavel  
