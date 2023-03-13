# Apontamentos para o projeto final de curso

## Objetivo

**Automatizar** processos de **anonimização** de dados usando a ferramenta ARX. Vão ser recebidos um conjunto de dados
de
entrada num formato de ficheiro CSV/Excel e o utilizador vai escolher o modelo de anonimização e privacidade que quer
aplicar. O programa vai gerar um ficheiro de saída com os dados anonimizados e um relatório com as estatísticas do
processo de anonimização. Resumindo:

1. **Aplicar** o modelo de privacidade ao conjunto de dados de entrada num processo automático.
2. **Quantificar** o risco da divulgação de informação sensível ou reidentificação.
3. **Avaliar** a utilidade dos dados.

## Motivação

A anonimização de dados é importante salvaguardar a privacidade dos indivíduos, impossibilitando a identificação direta
ou indireta de indivíduos a partir desses dados. Empresas e governos devem usar esses dados seguramente e responsável, e
há vários exemplos de vazamentos de dados que ilustram a importância da anonimização. Os casos mais conhecidos incluem a
*Equifax*, que sofreu um vazamento de dados em 2017, e o *Facebook*, em 2018. A falta de anonimização de dados pode
levar a problemas de privacidade e segurança, e as empresas devem implementar medidas adequadas de anonimização para
garantir a proteção dos dados sensíveis.

## Estado-da-arte

### Introdução

A anonimização de dados é um tema relevante na atualidade, especialmente para empresas e organizações que precisam lidar
com abundantes de informações sensíveis. Automatizar esse processo pode ser uma solução para facilitar a aplicação
de técnicas de anonimização em larga escala, selecionando as melhores abordagens e avaliando a qualidade dos dados
anonimizados.

Além disso, a automatização da anonimização de dados pode ajudar as empresas a cumprir as leis de proteção de dados,
como o Regulamento Geral sobre a Proteção de Dados (RGPD). Essas leis exigem que as empresas protejam os dados pessoais
dos indivíduos e adotem medidas adequadas de segurança e anonimização.

Neste capítulo, vai ser falado sobre a ferramenta que nos vai auxiliar na automatização do processo de anonimização de
dados, nas técnicas de anonimização e privacidade e nos modelos de privacidade.

### ARX - A Tool for Data Anonymization

ARX é uma ferramenta *open source* que permite a anonimização de dados sensíveis. Contém uma grande variedade de
algoritmos de anonimização, privacidade, transformação de dados e análise estatística. Contém também ferramentas para
gráficos, visualização de dados e exportação de dados. Neste projeto, vamos usar a API do ARX que entrega capacidades de
anonimização de dados para aplicação Java. Para a sua compreensão, vai ser usado a documentação disponibilizada pelo
ARX.

### Modelos de privacidade

#### Algoritmos de Agrupamento/ Clustering Algorithms

Os algoritmos de **agrupamento**, assim como o nome o diz, agrupam os dados para que os dados sensíveis não sejam
divulgados. Os algoritmos de agrupamento são baseados em 3 princípios:

##### *K-Anonymity*

No modelo *K-Anonymity*, os dados são **agrupados** para cada grupo ter pelo menos K elementos. O K é um valor definido
pelo utilizador. O *K-Anonymity* é um modelo de privacidade que garante que os dados sensíveis não são divulgados. No
entanto, o *K-Anonymity* não garante que os dados não sejam **reidentificados**.

##### *L-Diversity*

Este modelo de privacidade é uma **extensão** do *K-Anonymity*. No modelo *L-Diversity*, os dados são agrupados para
cada grupo ter pelo menos K elementos e que cada grupo tenha **pelo menos** L valores **distintos** para o atributo
sensível. O L é um valor definido pelo utilizador. O *L-Diversity* é um modelo de privacidade que garante que os dados
sensíveis não são divulgados. No entanto, o *L-Diversity* não garante que os dados não sejam **reidentificados**.

##### *T-Closeness*

Este modelo de privacidade é uma **extensão** do *L-Diversity*. No modelo *T-Closeness*, os dados são agrupados para
cada grupo ter pelo menos K elementos e que cada grupo tenha pelo menos L valores distintos para o atributo sensível.
Além disso, os dados são agrupados para que a diferença entre o valor mais frequente e o valor menos frequente para o
atributo sensível seja **menor ou igual a T**. O T é uma variável definida pelo utilizador. O *T-Closeness* é um modelo
de privacidade que garante que os dados sensíveis não são divulgados. No entanto, o *T-Closeness* não garante que os
dados não sejam **reidentificados**.

#### Privacidade Diferencial (*Differential Privacy*)

A privacidade diferencial é um modelo de privacidade que garante que os dados sensíveis não são divulgados e que os
dados não são reidentificados. O modelo de privacidade diferencial é baseado em 2 princípios:

##### Diferencial de Laplace

O modelo de privacidade diferencial de Laplace é um modelo de privacidade que garante que os dados sensíveis não são
divulgados e que os dados não são reidentificados. O modelo de privacidade diferencial de Laplace é baseado no princípio
de, que os dados sensíveis não são divulgados (reidentificados).

##### Diferencial de Gauss

O modelo de privacidade diferencial de Gauss é um modelo de privacidade que garante que os dados sensíveis não são
divulgados e que os dados não são reidentificados. O modelo de privacidade diferencial de Gauss é baseado no princípio
de, que os dados sensíveis não são divulgados (reidentificados).

## Engenharia de Software

### Requisítos funcionais

Estes são os requisítos funcionais que o sistema deve ter:

* [ ] O utilizador deve poder abrir e guardar ficheiros de dados.
* [ ] O utilizador deve poder escolher o formato de dados e o tipo de atributos.
* [ ] O utilizador deve poder escolher o(s) modelo(s) de privacidade e os seus parâmetros.
* [ ] O utilizador deve poder ter uma visualização dos dados.
* [ ] O utilizador deve ter uma análise estatítisca da segurança dos dados.
* [ ] O utilizador deve poder exportar os dados anonimizados e a análise estatística.
* [ ] O sistema deve descardar os dados considerados 'identifacadores'.
* [ ] O sistema tem que ser intuítivo e fácil de usar.
* [ ] O sistema tem que ser rápido e eficiente.

### Requisítos não funcionais

Estes são os requisítos não funcionais que o sistema deve ter:

* [ ] O sistema tem que ser seguro, e escalável.
* [ ] O sistema tem que usar a API do ARX.
* [ ] O sistema será feito em Java.
* [ ] O utilizador deve ser responsável por qualquer uso indevido dos dados.
* [ ] O sistema deve permitir o uso da anonimidade K, caso K seja maior que 2.
* [ ] O sistema deve permitir ao utilizador escolher qual das transformações de dados deve ser aplicada.
* [ ] O sistema deve execução estas operações num tempo aceitável, caso contrário, o sistema deve informar o utilizador
  que o tempo de execução é demasiado elevado.

### Interface

A ‘interface’ do sistema deve permitir que os utilizadores façam as seguintes tarefas:

* [ ] **Seleção de fonte de dados:** permitir que os utilizadores selecionem a fonte dos dados a serem anonimizados e
  analisados, podendo ser uma folha do Excel, um banco de dados, um arquivo CSV, ou outro formato compatível com o ARX.
* [ ] **Seleção de modelo de privacidade:** permitir que os utilizadores escolham o modelo de privacidade a ser aplicado
  na anonimização, como k-anonimato, l-diversidade, t-closeness, ou outro modelo disponível na API do ARX.
* [ ] **Configuração de parâmetros:** permitir que os utilizadores definam os parâmetros específicos do modelo de
  privacidade, como tamanho do grupo k, sensibilidade l, ou outros parâmetros relevantes para o modelo escolhido.
* [ ] **Visualização dos dados:** permitir que os utilizadores visualizem os dados a serem anonimizados e analisados,
  podendo ser exibidos em forma de tabela, gráficos ou outro modo de visualização apropriada.
* [ ] **Anonimização:** permitir que os utilizadores iniciem o processo de anonimização dos dados, usando o modelo de
  privacidade escolhido e os parâmetros configurados.
* [ ] **Análise exploratória:** permitir que os utilizadores realizem análises exploratórias nos dados anonimizados,
  como agrupamento, análise de tendências, entre outras técnicas disponíveis no ARX.
* [ ] **Exportação de dados:** permitir que os utilizadores exportem os dados anonimizados e as análises realizadas para
  outros formatos, como folhas do Excel, arquivos CSV ou outros formatos suportados pelo ARX.
* [ ] **Registo de atividades:** permitir que a ‘interface’ registe as atividades realizadas pelos utilizadores durante
  a anonimização e análise dos dados, para fins de rastreabilidade e auditoria.

## Primeiro Uso da API (Projeto em Terminal)

O primeiro uso da API do ARX será feito em terminal, para possibilitar a testagem da API e perceber como a mesma
funciona.
Estes são os passos que o utilizador deve seguir para anonimizar os dados, nesta primeira versão:

* [ ] Ler ficheiro de entrada
* [ ] Definir o tipo de metadados e o tipo de atributos
    * [ ] Caso seja um tipo 'sensivel', definir algoritmo de generalização/supressão (criar/exportar hierarquia)
    * [ ] Caso seja um tipo 'indentificador', eliminar o atributo
    * [ ] Caso seja um tipo 'quase identificador', definir algoritmo de generalização/supressão (criar/exportar
      hierarquia)
    * [ ] Caso seja um tipo 'insensivel', não fazer nada
* [ ] Definir o modelo de privacidade (Ele tem de ter pelo menos um atribusto quase identificador e os atributos
  sensiveis têm de ter o algoritmo de generalização/supressão definida)
    * [ ] Definir o tamanho do grupo (k-anonimato)
    * [ ] Caso desejado definir a diversidade (l-diversidade) e a diferença (t-closeness)
    * [ ] Devolver a tabelo dos dados anonimizados
* [ ] Guardar os ficheiro anonimizado e guardar as estatísticas numa variável

## Auxílios

Estas são as referências usadas para a realização deste projeto:

* [Facebook Data breach](https://www.cnbc.com/2018/10/12/facebook-security-breach-details.html)
* [Equifax Data breach](https://www.equifaxsecurity2017.com/)
* [Utility-driven assessment of anonymized data via clustering](https://www.nature.com/articles/s41597-022-01561-6)
* [Sobre a ferramenta](https://onlinelibrary.wiley.com/doi/10.1002/spe.2812)
* [Sobre a API](https://arx.deidentifier.org/development/api/)
* [Introdução da Ferramenta ARX](https://www.youtube.com/watch?v=N8I-sxmMfqQ&t=1s)
* [Documentação da API](https://arx.deidentifier.org/development/api/)