# 📱 DividiPix — Divisor de Despesas

## 📌 Sobre o projeto

O **DividiPix** é um aplicativo Android desenvolvido para facilitar o gerenciamento e a divisão de despesas compartilhadas entre várias pessoas.

A proposta é permitir que o usuário cadastre uma conta compartilhada, adicione os participantes envolvidos e registre os pagamentos realizados, acompanhando também quais valores estão pagos ou pendentes.

Os dados são armazenados no **Firebase Cloud Firestore**, permitindo que as informações cadastradas no aplicativo sejam persistidas em um banco de dados na nuvem.

---

## 🎯 Objetivo

O projeto foi desenvolvido com foco em um sistema simples e funcional de divisão de despesas, permitindo realizar operações de **CRUD (Create, Read, Update e Delete)** diretamente pelo aplicativo.

O usuário pode:

- Criar contas de despesas;
- Visualizar contas cadastradas;
- Editar contas;
- Excluir contas;
- Adicionar participantes;
- Editar participantes;
- Excluir participantes;
- Registrar pagamentos;
- Alterar pagamentos;
- Excluir pagamentos;
- Informar se um pagamento está pago ou pendente;
- Registrar uma referência de comprovante;
- Visualizar os dados armazenados no Firestore.

---

## 🛠️ Tecnologias utilizadas

- **Kotlin**
- **Android Studio**
- **Jetpack Compose**
- **Material 3**
- **Firebase**
- **Cloud Firestore**
- **Gradle Kotlin DSL**
- **Version Catalog**

### Configurações principais

- **Min SDK:** 24
- **Target SDK:** 36
- **Compile SDK:** 36
- **Java:** 11
- **Kotlin:** 2.0.21
- **Android Gradle Plugin:** 8.13.2

---

## 🔥 Firebase e Firestore

O projeto utiliza o **Firebase** como plataforma de serviços e o **Cloud Firestore** como banco de dados.

A estrutura principal do banco é composta por três coleções:

```text
Firestore
│
├── contas
├── participantes
└── pagamentos
```

### 📁 Contas

Armazena as despesas compartilhadas cadastradas no aplicativo.

Principais campos:

- `nome`
- `descricao`
- `valorTotal`
- `data`

### 📁 Participantes

Armazena as pessoas relacionadas a cada conta.

Principais campos:

- `contaId`
- `nome`
- `email`

### 📁 Pagamentos

Armazena os valores pagos ou pendentes por cada participante.

Principais campos:

- `contaId`
- `participanteId`
- `participanteNome`
- `valor`
- `descricao`
- `comprovante`
- `pago`

Os relacionamentos entre os dados são realizados através dos identificadores (`ID`) dos documentos.

---

## 🔄 Funcionamento do aplicativo

O fluxo principal do DividiPix funciona da seguinte maneira:

```text
Abrir aplicativo
       ↓
Tela inicial
       ↓
Criar ou selecionar uma conta
       ↓
Detalhes da conta
       ↓
┌───────────────────────┐
│ Participantes         │
│ Criar / Editar / Excluir
└───────────────────────┘
       ↓
┌───────────────────────┐
│ Pagamentos            │
│ Criar / Editar / Excluir
└───────────────────────┘
       ↓
Firebase Cloud Firestore
```

### 🏠 Tela inicial

Apresenta as contas cadastradas, a quantidade de contas e o valor total das despesas.

### 💰 Contas

Cada conta representa uma despesa compartilhada, como uma viagem, churrasco, evento ou qualquer outra situação em que os gastos sejam divididos entre pessoas.

### 👥 Participantes

Permite cadastrar as pessoas que participam daquela despesa.

### 💸 Pagamentos

Permite registrar quanto cada participante pagou, adicionar uma descrição, informar uma referência de comprovante e definir o status como **pago** ou **pendente**.

---

## 🗄️ Arquitetura básica

O projeto utiliza uma separação simples entre modelos, repositório e interface.

```text
com.example.dividipix
│
├── data
│   └── model
│       ├── Conta.kt
│       ├── Participante.kt
│       └── Pagamento.kt
│
├── repository
│   └── FirestoreRepository.kt
│
├── ui
│   ├── components
│   ├── screens
│   └── theme
│
└── MainActivity.kt
```

O `FirestoreRepository` concentra as operações de comunicação com o Firestore, enquanto as telas utilizam essas funções para realizar as operações de CRUD.

---

## 🔐 Autenticação

O projeto **não utiliza Firebase Authentication**.

A atividade tem como foco o gerenciamento das despesas e a persistência dos dados utilizando o **Cloud Firestore**. Por esse motivo, o aplicativo inicia diretamente na tela principal, sem exigir login ou cadastro de usuário.

---

## ▶️ Como executar o projeto

### 1. Clonar o repositório

```bash
git clone URL_DO_REPOSITORIO
```

### 2. Abrir no Android Studio

Abra a pasta do projeto no **Android Studio** e aguarde a sincronização do Gradle.

### 3. Configurar o Firebase

Para conectar o aplicativo ao Firebase, é necessário possuir um projeto Firebase configurado e adicionar o arquivo:

```text
app/google-services.json
```

O arquivo `google-services.json` é específico do projeto Firebase e deve ser obtido pelo Firebase Console.

### 4. Executar

Conecte um dispositivo Android ou utilize um emulador e execute o projeto pelo Android Studio.

---

## 🧪 Demonstração do CRUD

O funcionamento do projeto pode ser demonstrado através do seguinte fluxo:

1. Criar uma conta de despesa;
2. Verificar a conta criada no Firestore;
3. Editar os dados da conta;
4. Adicionar participantes;
5. Editar e excluir participantes;
6. Criar pagamentos;
7. Alterar o status dos pagamentos;
8. Editar e excluir pagamentos;
9. Conferir as alterações diretamente no Cloud Firestore.

---

## 📸 Prints do projeto

### 🏠 Tela inicial

> *Espaço destinado à captura de tela da tela inicial do aplicativo.*

### 💰 Cadastro e gerenciamento de contas

> *Espaço destinado às capturas das telas de criação, edição e detalhes das contas.*

### 👥 Participantes

> *Espaço destinado às capturas das telas de participantes.*

### 💸 Pagamentos

> *Espaço destinado às capturas das telas de pagamentos.*

### 🔥 Firebase Firestore

> *Espaço destinado às capturas do banco de dados no Firebase Console, mostrando as coleções e os documentos criados pelo aplicativo.*

---

## 🎥 Vídeo de demonstração

> **Link do vídeo:**  
> [Adicionar link do vídeo aqui]

O vídeo deve apresentar o funcionamento do aplicativo, incluindo a criação e gerenciamento das despesas e a visualização dos dados no Firebase Firestore.

---

## 👨‍💻 Desenvolvedor

**Renan Pereira Rezaghi**

Projeto acadêmico desenvolvido para demonstração de desenvolvimento Android, integração com Firebase e operações de CRUD utilizando Cloud Firestore.
