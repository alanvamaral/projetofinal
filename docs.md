# Projeto Final: Sistema Bancário em Java

## 📋 Sobre o Projeto
Este projeto consiste no desenvolvimento de um sistema bancário **Full-Stack** utilizando a linguagem Java. [cite_start]O objetivo é aplicar os princípios da **Programação Orientada a Objetos (POO)**, criar uma interface gráfica (GUI) intuitiva e garantir a persistência dos dados através de um banco de dados MySQL[cite: 2, 3].

[cite_start]O sistema é modular, escalável e implementa operações bancárias essenciais como cadastro, depósito, saque e transferências[cite: 6].

---

## 🛠️ Tecnologias e Conceitos
* **Linguagem:** Java (JDK atualizado).
* [cite_start]**Interface Gráfica:** Java Swing[cite: 8].
* **Banco de Dados:** MySQL.
* [cite_start]**Conexão:** JDBC (Java Database Connectivity)[cite: 8].
* **Conceitos:** Herança, Polimorfismo, Encapsulamento, Tratamento de Exceções e Coleções (Maps/Lists).

---

## 📂 Estrutura do Projeto
[cite_start]O projeto segue a convenção de pacotes com domínio reverso `com.unileste.projetofinal` e utiliza o padrão **CamelCase** para classes e **lowercase** para pacotes[cite: 11, 12].

### 1. Pacote `entidades`
Responsável pela modelagem dos dados do negócio.

* **`Cliente`**: Representa o correntista.
    * [cite_start]*Atributos:* `nome`, `cpf` (id único), `endereco`, `List<Conta> contas` [cite: 48-52].
    * [cite_start]*Métodos:* Validar dados no construtor, adicionar conta, `equals` (pelo CPF) e `toString` [cite: 55-62].
* **`Conta` (Abstrata)**: Base para os tipos de conta.
    * [cite_start]*Atributos:* `numero`, `saldo` (protected), `Cliente proprietario`, `historicoTransacoes` [cite: 66-69].
    * [cite_start]*Métodos Abstratos:* `depositar()`, `sacar()`, `transferir()` [cite: 75-79].
* **`ContaCorrente`**: Subclasse de Conta.
    * *Diferencial:* Possui `limiteChequeEspecial`. [cite_start]O saque consome o saldo + limite[cite: 85, 95].
* **`ContaPoupanca`**: Subclasse de Conta.
    * [cite_start]*Diferencial:* Lógica de saque específica (sem cheque especial)[cite: 109].

### 2. Pacote `operacoes`
Contém a lógica de negócios central (o "cérebro" do sistema).

* **`Banco`**: Gerencia clientes e contas.
    * [cite_start]*Armazenamento:* Usa `Map<String, Cliente>` (Chave: CPF) e `Map<String, Conta>` (Chave: Número) para busca rápida [cite: 120-122].
    * [cite_start]*Funcionalidades:* Cadastrar cliente, abrir conta, realizar saques, depósitos e transferências delegando para as entidades [cite: 127-137].

### 3. Pacote `utilitarios`
[cite_start]Classes auxiliares e exceções personalizadas para tratamento de erros semânticos [cite: 22-25].

* `ClienteNaoEncontradoException`
* `ContaNaoEncontradaException`
* `SaldoInsuficienteException`

### 4. Pacote `dao` (Data Access Object)
[cite_start]Responsável pela comunicação com o banco de dados MySQL via JDBC [cite: 26-31].

* `ClienteDAO` / `ClienteDAOJdbc`: CRUD de clientes.
* `ContaDAO` / `ContaDAOJdbc`: CRUD de contas e atualização de saldos.

### 5. Pacote `gui`
[cite_start]Interface Gráfica do Usuário construída com Swing [cite: 32-37].

* `MainFrame`: Janela principal.
* Painéis: `ClientePanel`, `ContaPanel`, `OperacoesPanel`.

---

## 📹 Diretrizes da Apresentação (Vídeo)

Para a avaliação final, o grupo deve entregar um vídeo explicativo hospedado no **YouTube** (configurado como "Não Listado" ou "Privado").

### 🔴 Regras de Formatação
* **Duração Máxima:** 15 minutos.
* **Identificação:** O vídeo **DEVE** iniciar com uma tela contendo o **Nome Completo** e **RA** de todos os integrantes escritos (não apenas falados).
* **Apresentador:** Pode ser gravado por apenas um aluno ou pelo grupo todo. Não é necessário aparecer na câmera.
* **Áudio:** Garantir clareza e qualidade na explicação.

### 🎬 Roteiro de Demonstração (Obrigatório)
O vídeo deve conter a demonstração do sistema nesta ordem exata:

1.  [ ] **Cadastrar Cliente:** Inserir um novo cliente no sistema.
2.  [ ] **Cadastrar Conta:** Criar uma conta (Corrente ou Poupança) vinculada ao cliente criado acima.
3.  [ ] **Depósito Inicial:** Realizar um depósito para inserir saldo na conta.
4.  [ ] **Verificação de Saldo (1):** Exibir o saldo atualizado após o depósito.
5.  [ ] **Saque:** Realizar uma retirada de valor da conta.
6.  [ ] **Verificação de Saldo (2):** Exibir o saldo final após a retirada.

---

## ✅ Checklist de Desenvolvimento
[cite_start]Este roteiro baseia-se na avaliação individual proposta [cite: 200-227].

- [ ] **Modelagem (Aluno 1):** Diagrama UML, criação das classes `Cliente`, `Conta` e subclasses.
- [ ] **Regras de Negócio (Aluno 2):** Implementação da classe `Banco` e lógica dos Mapas.
- [ ] **Persistência (Aluno 3):** Configuração do JDBC e implementação das classes DAO.
- [ ] **Interface Gráfica (Aluno 4):** Telas de cadastro e operações com Swing.
- [ ] **Testes e Exceções (Aluno 5):** Criação das Exceptions personalizadas e validação dos fluxos (try-catch).

---

## ⚙️ Como Rodar
1.  Configure o banco de dados MySQL (crie o schema e as tabelas conforme as entidades).
2.  Configure a string de conexão JDBC no pacote `dao`.
3.  Compile o projeto.
4.  [cite_start]Execute a classe `AppProjetoFinal.java` para iniciar a aplicação[cite: 39].