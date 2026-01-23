**moodSummary** is a full-stack Spring Boot application that turns your daily diary entries into abstract art. Instead of just writing down "I had a long day," the app uses Google's Gemini AI to analyze your emotions and generate a unique piece of digital art representing your mood.

A feature I enjoyed building is the **emotional collage.** The app looks at the k most recent text entries in the database, analyzes the emotional arc, and generates an image representing the combination of the k entries.

## Work flow:

* **Dual-AI Orchestration:** Combines a text LLM (Gemini Pro) to understand your feelings with an Image Generator (Imagen) to visualize them.
* **The "Collage" Feature:** By storing history in PostgreSQL, the app can look back at your last k entries to summarize how your mood has evolved.
* **Frontend:** JavaScript + HTML5

## Stack:

* **Backend:** Java 21 & Spring Boot (MVC)
* **AI Integration:** Spring AI with the Google Gemini API
* **Database:** PostgreSQL (running in Docker) with Spring Data JPA/Hibernate
* **Testing:** JUnit 5 & Mockito

## Lessons:

* Standard database columns limit text to 255 characters. I learned that AI outputs can easily blow past this, crashing the app. I re-engineered the Hibernate schema to use PostgreSQL TEXT types for unbounded storage.

  * For an extension, I would likely have to add a character limit to prevent abuse.
* **Testing AI is tricky:** You shouldn't make real API calls in unit tests. I used Mockito's Deep Stubs to mock the fluent API chains of the Spring AI ChatClient.

---

## Quick Start:

In main/resources/applications.properties, input your

spring.ai.google.genai.api-key=
spring.ai.google.genai.project-id=

Start the PostgreSQL container:

**Bash**

```
docker run --name mood-db \
  -e POSTGRES_USER=myuser \
  -e POSTGRES_PASSWORD=mypassword \
  -e POSTGRES_DB=mood_gallery \
  -p 5432:5432 \
  -d postgres:16
```

```
./gradlew bootRun
```

*hosted on  `http://localhost:8080`*

---

## Testing:

I used **Test-Driven Development (TDD)** principles for the core business logic. Run the test suite using:

**Bash**

```
./gradlew test
```

*Note: The tests use Constructor Injection and Mockito mocks, meaning they don't require an active database or internet connection to pass.*
