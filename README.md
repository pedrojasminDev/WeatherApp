# 🌤️ WeatherApp

![Kotlin](https://img.shields.io/badge/Kotlin-2.0.0-purple)
![Android](https://img.shields.io/badge/Android-7.0+-green)
![API](https://img.shields.io/badge/OpenWeatherMap-API-blue)
![License](https://img.shields.io/badge/License-MIT-yellow)

Aplicativo Android nativo desenvolvido em **Kotlin** que consome a API da OpenWeatherMap para exibir informações climáticas em tempo real de qualquer cidade do mundo.

---

## 📱 Funcionalidades

| Funcionalidade | Descrição |
|----------------|-----------|
| 🔍 **Busca por cidade** | Digite o nome de qualquer cidade do mundo |
| 🌡️ **Temperatura atual** | Exibe a temperatura em graus Celsius |
| 💧 **Umidade do ar** | Mostra a porcentagem de umidade |
| ☁️ **Descrição do clima** | Texto descritivo (ex: "céu limpo", "nublado") |
| 🎨 **Interface moderna** | Layout limpo com CardView |

---

## 🛠️ Tecnologias utilizadas

| Tecnologia | Finalidade |
|------------|------------|
| **Kotlin** | Linguagem principal do app |
| **Retrofit2** | Consumo de API REST |
| **Gson** | Parsing de JSON |
| **Coroutines** | Requisições assíncronas (não travam a UI) |
| **ViewModel** | Gerenciamento de dados (MVVM) |
| **CardView** | Design moderno da interface |


---

## 🚀 Como executar o projeto

### Pré-requisitos

- Android Studio (versão mais recente recomendada)
- Dispositivo Android ou emulador (API 24+)
- Conexão com internet

### Passo a passo

1. **Clone o repositório**
   ```bash
   git clone https://github.com/pedrojasminDev/WeatherApp.git
   
2. **Abra o projeto no Android Studio**
   - File → Open → Selecione a pasta do projeto
  
3. **Obtenha uma chave de API gratuita**
   - Acesse OpenWeatherMap
   - Crie uma conta (grátis)
   - Vá em "My API Keys" e copie sua chave
   - ⚠️ A ativação pode levar de 2 a 4 horas

4. **Adicione sua chave no código**
   - Abra MainActivity.kt
   - Localize esta linha:
       - private val API_KEY = "SUA_API_KEY_AQUI"
   - Substitua pela sua chave:
       - private val API_KEY = "a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6"

5. **Execute o app**
   - Conecte seu celular via USB (com Depuração USB ativada)
   - Ou use um emulador
   - Clique no botaão ▶️ Run

6. **Teste**
   - Digite uma cidade (ex: "São Paulo", "Londres")
   - Clique em "Buscar Clima"
   - Veja o resultado! 🎉
  
---

## 🔧 Possíveis problemas e soluções

| Problema | Solução |
|----------|---------|
| **"Cidade não encontrada"** | Verifique a ortografia. Use nomes em inglês ("London" em vez de "Londres") |
| **Erro de rede** | Confirme se adicionou INTERNET permission no AndroidManifest.xml |
| **API Key inválida** | Aguarde 2-4 horas após criar a conta. A chave leva tempo para ativar |
| **App não compila** | Clique em File → Sync Project with Gradle Files |
| **CardView não aparece** | Adicione a dependência: implementation("androidx.cardview:cardview:1.0.0") |

---

## 📚 O que aprendi com este projeto
- ✅ Consumo de APIs REST no Android
- ✅ Requisições assíncronas com Coroutines
- ✅ Tratamento de estados (loading, sucesso, erro)
- ✅ Boas práticas de UI/UX
- ✅ Controle de versão com Git/GitHub
- ✅ Documentação de projetos

---

## 🔜 Possíveis melhorias
- Adicionar ícone do clima dinâmico
- Previsão para 5 dias
- Detectar localização automática (GPS)
- Salvar última cidade pesquisada
- Tema claro/escuro
- Tela de Splash

--- 

## 🤝 Contribuição

Sinta-se à vontade para contribuir com melhorias!

1. Faça um fork do projeto
2. Crie uma branch: git checkout -b minha-melhoria
3. Commit suas mudanças: git commit -m 'Adiciona funcionalidade X'
4. Push: git push origin minha-melhoria
5. Abra um Pull Request

---

### 📄 Licença
Este projeto está sob a licença MIT. Veja o arquivo LICENSE para mais detalhes.

---

### 👨‍💻 Autor

Pedro Luiz da Silva Jasmin

- GitHub: @pedrojasminDev
- LinkedIn: Pedro Luiz Jasmin
- E-mail: pluiz.jasmin@gmail.com

---

### 🙏 Agradecimentos

- OpenWeatherMap pela API gratuita
- Retrofit pela biblioteca de requisições
- Meu professor Aparecido Valdemir de Freitas pela orientação no projeto
