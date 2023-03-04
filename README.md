# Apontamentos para o projeto final de curso

## Objetivo
**Automatizar** processos de **anonimação** de dados usando a ferramenta ARX. Vão ser recebidos um conjunto de dados de entrada num formato de ficheiro CSV/Excel e o utilizador vai escolher o modelo de anonimação e privacidade que quer aplicar. O programa vai gerar um ficheiro de saída com os dados anonimizados e um relatório com as estatísticas do processo de anonimação. Resumindo:
1. Aplicar o modelo de privacidade ao conjunto de dados de entrada.
2. Quantificar o risco divulgação de informação sensível ou reidentificação.
3. Avaliar a utilidade dos dados.

## Modelos de privacidade

### Algoritmos de Agrupamento/ Clustering Algorithms
Os algoritmos de agrupamento, assim como o nome o diz, agrupam os dados para que os dados sensíveis não sejam divulgados. Os algoritmos de agrupamento são baseados em 3 princípios:

#### K-Anonymity
No modelo K-Anonymity, os dados são agrupados para cada grupo ter pelo menos K elementos. O K é um valor definido pelo utilizador. O K-Anonymity é um modelo de privacidade que garante que os dados sensíveis não são divulgados. No entanto, o K-Anonymity não garante que os dados não sejam reidentificados.

#### L-Diversity
Este modelo de privacidade é uma extensão do K-Anonymity. No modelo L-Diversity, os dados são agrupados para cada grupo ter pelo menos K elementos e que cada grupo tenha pelo menos L valores distintos para o atributo sensível. O L é um valor definido pelo utilizador. O L-Diversity é um modelo de privacidade que garante que os dados sensíveis não são divulgados. No entanto, o L-Diversity não garante que os dados não sejam reidentificados.

#### T-Closeness
Este modelo de privacidade é uma extensão do L-Diversity. No modelo T-Closeness, os dados são agrupados para cada grupo ter pelo menos K elementos e que cada grupo tenha pelo menos L valores distintos para o atributo sensível. Além disso, os dados são agrupados para que a diferença entre o valor mais frequente e o valor menos frequente para o atributo sensível seja menor ou igual a T. O T é um valor definido pelo utilizador. O T-Closeness é um modelo de privacidade que garante que os dados sensíveis não são divulgados. No entanto, o T-Closeness não garante que os dados não sejam reidentificados.

### Privacidade Diferencial (Differential Privacy)
A privacidade diferencial é um modelo de privacidade que garante que os dados sensíveis não são divulgados e que os dados não são reidentificados. O modelo de privacidade diferencial é baseado em 2 princípios:

#### Diferencial de Laplace
O modelo de privacidade diferencial de Laplace é um modelo de privacidade que garante que os dados sensíveis não são divulgados e que os dados não são reidentificados. O modelo de privacidade diferencial de Laplace é baseado no princípio de que os dados sensíveis não são divulgados. O modelo de privacidade diferencial de Laplace é baseado no princípio de que os dados não são reidentificados.

#### Diferencial de Gauss
O modelo de privacidade diferencial de Gauss é um modelo de privacidade que garante que os dados sensíveis não são divulgados e que os dados não são reidentificados. O modelo de privacidade diferencial de Gauss é baseado no princípio de que os dados sensíveis não são divulgados. O modelo de privacidade diferencial de Gauss é baseado no princípio de que os dados não são reidentificados.

# Requisitos
* [ ] Ler ficheiro de entrada
* [ ] Escolher modelo de privacidade (Algoritmos de agrupamento/clustering ou privacidade diferencial)
* [ ] Por ver...

# Interface
* [ ] Seleção de fonte de dados: permitir que os utilizadores selecionem a fonte dos dados a serem anonimizados e analisados, podendo ser uma folhas do Excel, um banco de dados, um arquivo CSV, ou outro formato compatível com o ARX.

* [ ] Seleção de modelo de privacidade: permitir que os utilizadores escolham o modelo de privacidade a ser aplicado na anonimização, como k-anonimato, l-diversidade, t-closeness, ou outro modelo disponível na API do ARX.

* [ ] Configuração de parâmetros: permitir que os utilizadores definam os parâmetros específicos do modelo de privacidade, como tamanho do grupo k, sensibilidade l, ou outros parâmetros relevantes para o modelo escolhido.

* [ ] Visualização dos dados: permitir que os utilizadores visualizem os dados a serem anonimizados e analisados, podendo ser exibidos em forma de tabela, gráficos ou outro tipo de visualização apropriada.

* [ ] Anonimização: permitir que os utilizadores iniciem o processo de anonimização dos dados, usando o modelo de privacidade escolhido e os parâmetros configurados.

* [ ] Análise exploratória: permitir que os utilizadores realizem análises exploratórias nos dados anonimizados, como agrupamento, análise de tendências, entre outras técnicas disponíveis no ARX.

* [ ] Exportação de dados: permitir que os utilizadores exportem os dados anonimizados e as análises realizadas para outros formatos, como folhas do Excel, arquivos CSV ou outros formatos suportados pelo ARX.

* [ ] Registo de atividades: permitir que a interface registe as atividades realizadas pelos utilizadores durante a anonimização e análise dos dados, para fins de rastreabilidade e auditoria.


# Auxílios
* [Utility-driven assessment of anonymized data via clustering](https://www.nature.com/articles/s41597-022-01561-6)
* [Sobre a ferramenta](https://onlinelibrary.wiley.com/doi/10.1002/spe.2812)
* [Sobre a API](https://arx.deidentifier.org/development/api/)
* [Introdução da Ferramenta ARX](https://www.youtube.com/watch?v=N8I-sxmMfqQ&t=1s)