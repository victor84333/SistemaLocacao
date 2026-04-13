# 🚗 Sistema de Locação de Veículos

Projeto acadêmico em Java — POO com persistência MySQL e interface Swing.

---

## 📁 Estrutura de Pacotes

```
src/
├── Main.java                    ← Ponto de entrada
├── model/
│   ├── Categoria.java           ← Enum com categorias e valores
│   ├── Cliente.java
│   ├── Veiculo.java
│   └── Locacao.java
├── dao/
│   ├── ClienteDAO.java
│   ├── VeiculoDAO.java
│   └── LocacaoDAO.java
├── util/
│   └── Conexao.java             ← JDBC connection
└── view/
    └── MainFrame.java           ← Interface Swing (JFrame)

sql/
└── locadora.sql                 ← Script de criação do banco
```

---

## ⚙️ Pré-requisitos

- JDK 11+
- XAMPP iniciado com MySQL(Cria um ambiente de servidor web local no seu computador)
- `mysql-connector-j-8.x.x.jar` no classpath do projeto (usado intellij)

---

## 🗄️ Configuração do Banco

1. Inicie o XAMPP e ative o **MySQL**.
2. Acesse `http://localhost/phpmyadmin`
3. Clique em **"Nova"** ou **"Importar"** e execute o arquivo `sql/locadora.sql`
4. O banco `locadora` será criado com as tabelas e dados de exemplo

---

## 🚀 Compilar e Executar

### 1. Baixe o conector MySQL

Faça download do `mysql-connector-j-8.x.x.jar` em:
https://dev.mysql.com/downloads/connector/j/

### 2. Compile (coloque o .jar na mesma pasta ou em lib/) ou apenas execute no intelliJ

```bash
# Windows
javac -cp ".;mysql-connector-j-8.x.x.jar" -d out -sourcepath src src/Main.java

# Linux/Mac
javac -cp ".:mysql-connector-j-8.x.x.jar" -d out -sourcepath src src/Main.java
```

### 3. Execute

Para executar o projeto:

1.Certifique-se de que o banco de dados esteja ativo no XAMPP
2.Garanta que o driver MySQL Connector/J esteja corretamente adicionado ao projeto
3.Execute a classe Main.java pela sua IDE, a parte gráfica será aberta automáticamente

A aplicação será iniciada automaticamente com a interface gráfica.

---

## 💰 Tabela de Preços por Categoria

| Cat | Descrição          | Diária (R$) | Por KM (R$) |
|-----|--------------------|-------------|-------------|
| A   | Hatch Compacto     | 50,00       | 0,25        |
| B   | Sedan Intermediário| 70,00       | 0,30        |
| C   | SUV Compacto       | 90,00       | 0,35        |
| D   | Sedan Executivo    | 120,00      | 0,45        |
| E   | SUV Executivo      | 150,00      | 0,55        |

**Fórmula:** `Valor Total = (Diária × Nº Diárias) + (R$/KM × KM Percorrida)`

---

## 🧭 Como Usar a Interface

1. **Aba Clientes** → Cadastre/atualize clientes (nome + CPF)
2. **Aba Veículos** → Cadastre veículos (marca, modelo, categoria, KM)
3. **Aba Locações:**
   - **Nova Locação** → escolha cliente, veículo e nº de diárias
   - **Registrar Devolução** → selecione a locação em aberto e informe o KM final
   - O valor é calculado automaticamente e exibido em uma mensagem

---

## 🔌 Conexão (Conexao.java)

```java
URL:     jdbc:mysql://localhost:3306/locadora
Usuário: root
Senha:   (vazia)
```

Altere estas configurações se necessário.
