# LABSky Linhas Aéreas - Backend
## Introdução
Projeto de recuperação do modulo 3 do programa de formação DEVinHouse. 
Desenvolvido por:

- Danilo Paes Ribeiro (https://github.com/danilopaes)

Contexto: A LABSky Linhas Aéreas entrou em operação com uma aeronave para atender a um grupo seleto de clientes que fazem o trecho Florianópolis/SC - Santa Maria/RS diariamente. Você foi escolhido para criar o back-end de uma aplicação para gerenciar os passageiros que irão no voo.

## Tecnologias & Versões
- **Java** - versão 17;
- **Spring Boot** - versão 2.7.14-SNAPSHOT;
- **Maven** - versão 4.0.0;
- **ModelMapper** - versão 3.0.0;
- **Banco de dados SQL embarcado (H2)**;- 
- Cobertura de testes unitários da camada Service utilizando **JUnit** e **Mockito**;

## Rotas & Funcionalidades
### Consulta de Passageiros
Rota responsável pela consulta de todos os passageiros cadastrados no voo.
Deve retorn os dados atualizados de todos os passageiros.

HTTP GET → http://localhost:8080/api/passageiros

### Consulta de Passageiros Pendentes de confirmação
Rota responsável por retornar somente os dados dos passageiros que estão
pendentes de confirmação no voo, ou seja, somente aqueles que ainda não fizeram
check-in.

HTTP GET → http://localhost:8080/api/passageiros/pendentes

### Consulta de Assentos
Rota responsável por retornar todos os assentos da aeronave indicando condição se já foi
alocado para um passageiro.

HTTP GET → http://localhost:8080/api/assentos

### Consulta de Confirmação
Rota responsável por retornar os dados da confirmação (e-ticket) informada.
Campos da requisição: O valor do e-ticket no path (URL) do serviço.

HTTP GET → http://localhost:8080/api/passageiros/confirmacao/{eticket}

### Realização de Confirmação (check-in)
Rota responsável por registrar uma confirmação para determinado passageiro.
Recebe como parâmetro o passageiro, o assento e se deseja despachar mala.
Caso a solicitação não infrinja nenhuma regra de negócio, um cartão de confirmação
será gerado.

HTTP POST → http://localhost:8080/api/passageiros/confirmacao

### Sistema de Milhas
A empresa também tem um “programa de fidelidade” para os clientes, onde a cada
voo o passageiro acumula milhas que podem posteriormente ser trocados por brindes
ou descontos em passagens. As classificações de uma passageiro no programa
de fidelidade da empresa podem ser as seguintes: VIP, OURO, PRATA, BRONZE, 
ASSOCIADO.
