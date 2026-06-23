# Meta Post Performance Analysis

Spring Boot servisi — Meta (Facebook) Graph API vasitəsilə bir səhifənin son postlarını çəkir, PostgreSQL-də saxlayır və **engagement (likes + comments)** əsasında performans analizi aparır: ən yüksək performanslı postlar, həftənin günlərinə görə likes statistikası, və ən "məhsuldar" gün.

## 🚀 Əsas Xüsusiyyətlər

- **Meta Graph API inteqrasiyası** — Feign Client vasitəsilə `v25.0` endpoint-indən səhifə feed-ini çəkir
- **Avtomatik sinxronizasiya** — `POST /api/posts/sync/post` ilə Meta-dan son postları çəkib bazaya yazır (upsert məntiqi — mövcud post yenilənir, yeni post əlavə olunur)
- **Engagement analizi**:
  - Top N ən çox engagement alan post (likes + comments)
  - Həftənin günlərinə görə cəmi likes
  - Ən yaxşı performans göstərən gün
  - Mətn formasında ümumi nəticə (insight)
- **Keş (Caching)** — Spring Cache ilə (`@Cacheable`) hesablama nəticələri keşlənir, yeni sync zamanı (`@CacheEvict`) avtomatik təmizlənir
- **Liquibase** — verilənlər bazası sxemi migration vasitəsilə idarə olunur
- **Docker Compose** — tətbiq və PostgreSQL bir əmrlə qaldırılır

## 🛠️ Texnologiya Stack-i

| Kateqoriya | Texnologiya |
|---|---|
| Dil / Runtime | Java 21 |
| Framework | Spring Boot 4.0.3 |
| Verilənlər bazası | PostgreSQL 16 |
| Migration | Liquibase |
| HTTP Client | Spring Cloud OpenFeign |
| ORM | Spring Data JPA / Hibernate |
| Mapping | MapStruct |
| Boilerplate | Lombok |
| Konteynerləşdirmə | Docker / Docker Compose |
| Connection Pool | HikariCP |

## 📁 Layihə Strukturu

```
src/main/java/com/project/metapostperformanceanalysis/
├── client/          # Feign client (Meta Graph API)
├── config/          # Feign konfiqurasiyası (auth interceptor)
├── controller/      # REST endpoint-lər
├── dto/
│   ├── request/     # Daxil olan request body-lər
│   └── response/    # Xarici cavab modelləri (Meta API + öz API-miz)
├── entity/          # JPA entity-lər
├── exception/       # Global exception handling
├── mapper/          # MapStruct mapper-lər
├── repository/      # Spring Data JPA repository-lər
└── service/         # Biznes məntiqi
```

## ⚙️ Quraşdırma

### Tələblər

- Java 21+
- Maven
- Docker & Docker Compose
- Meta Developer hesabı (App ID, App Secret, Page Access Token, Page ID)

### 1. Repo-nu klonlayın

```bash
git clone https://github.com/Meta-Graph-project/meta-post-performance-analysis.git
cd meta-post-performance-analysis
```

### 2. `.env` faylını yaradın

Layihənin kök qovluğunda (`docker-compose.yaml` ilə **eyni qovluqda**) `.env` faylı yaradın:

```env
POSTGRES_PASSWORD=your_db_password

META_APP_ID=your_meta_app_id
META_APP_SECRET=your_meta_app_secret
META_PAGE_ID=your_meta_page_id
META_PAGE_ACCESS_TOKEN=your_meta_page_access_token
```

> ⚠️ **Vacib:** `.env` faylı `.gitignore`-da olmalıdır və **heç vaxt** repo-ya commit edilməməlidir. Meta token/secret sızması bütün səhifəyə girişin kompromis olmasına səbəb ola bilər — sızma şübhəsi olarsa, Meta Developer Dashboard-dan dərhal rotasiya edin.

### 3. Docker Compose ilə qaldırın

```bash
docker compose up -d --build
```

Bu əmr iki konteyner qaldıracaq:
- `meta-performance` — PostgreSQL 16
- `meta-app` — Spring Boot tətbiqi (port `8081` → konteyner daxilində `8080`)

### 4. İşlədiyini yoxlayın

```bash
docker compose logs -f app
```

Tətbiq `http://localhost:8081` ünvanında işə düşməlidir.

## 🔌 API Endpoint-ləri

| Method | Endpoint | Təsvir |
|---|---|---|
| `POST` | `/api/posts/sync/post` | Meta-dan son postları çəkib bazaya yazır |
| `GET` | `/api/posts` | Bazadakı bütün postları qaytarır |
| `GET` | `/api/posts/{id}` | ID-yə görə tək post |
| `GET` | `/api/posts/top?limit=3` | Ən yüksək engagement-li N post |
| `GET` | `/api/posts/likes-by-day` | Həftənin günlərinə görə cəmi likes |
| `GET` | `/api/posts/best-day` | Ən yaxşı performans göstərən gün |
| `GET` | `/api/posts/analysis` | Tam analiz hesabatı (top 3, günlük statistika, nəticə mətni) |

### Tipik istifadə axını

```bash
# 1. Əvvəlcə Meta-dan datanı çəkin
curl -X POST http://localhost:8081/api/posts/sync/post

# 2. Sonra analiz hesabatını alın
curl http://localhost:8081/api/posts/analysis
```

## 🔐 Environment Dəyişənləri

| Dəyişən | Təsvir |
|---|---|
| `POSTGRES_PASSWORD` | PostgreSQL parolu |
| `META_APP_ID` | Meta Developer App ID |
| `META_APP_SECRET` | Meta Developer App Secret |
| `META_PAGE_ID` | Analiz olunacaq Facebook səhifəsinin ID-si |
| `META_PAGE_ACCESS_TOKEN` | Səhifə üçün uzunömürlü Access Token |

> **Qeyd:** `MetaFeignConfig` sinfi hazırda `pageId`-ni `META_APP_PAGE_ID` adlı placeholder ilə oxuyur, halbuki `application.yml`-də və `.env`-də dəyişən `META_PAGE_ID` adlanır. Tətbiqi işə salmazdan əvvəl bu iki adın **uyğunlaşdırıldığını** yoxlayın (hər ikisini eyni ada gətirin), əks halda `BeanCreationException: Could not resolve placeholder` xətası alacaqsınız.

## 🧠 Engagement Hesablama Məntiqi

```
Engagement = likeCount + commentCount
```

- **Ən yaxşı gün** — bütün postlar `DayOfWeek`-ə görə qruplaşdırılır, hər gün üzrə likes cəmlənir, ən yüksək nəticəyə malik gün seçilir.
- **Top postlar** — verilənlər bazası səviyyəsində SQL sorğusu ilə (`likeCount + commentCount`) azalan sırada sıralanır.

## 🗄️ Verilənlər Bazası Sxemi

| Sütun | Tip | Təsvir |
|---|---|---|
| `id` | `BIGINT` (PK, auto) | Daxili identifikator |
| `meta_post_id` | `VARCHAR` (unique) | Meta-dan gələn post ID-si (dublikat qarşısını alır) |
| `message` | `TEXT` | Post mətni |
| `created_time` | `TIMESTAMP` | Postun yaradılma tarixi |
| `like_count` | `INTEGER` | Bəyənmə sayı |
| `comment_count` | `INTEGER` | Şərh sayı |

Sxem dəyişiklikləri Liquibase changelog-u (`classpath:/db/changelog/db.changelog.yaml`) ilə idarə olunur.

## 🐳 Docker Compose Strukturu

```yaml
services:
  postgres:   # PostgreSQL 16, healthcheck ilə
  app:        # Spring Boot tətbiqi, postgres "healthy" olana qədər gözləyir
```

`app` servisi `depends_on: condition: service_healthy` istifadə edir — yəni Postgres tam hazır olmadan tətbiq başlamır, bu da tipik "Connection refused" xətalarının qarşısını alır.

## 🩹 Tez-tez Rast Gəlinən Problemlər

| Simptom | Səbəb | Həll |
|---|---|---|
| `Connection to localhost:5432 refused` | Datasource URL-də `localhost` hardcoded qalıb, Docker şəbəkəsində bu konteynerin öz daxilini göstərir | `application.yml`-də `postgres` servis adından istifadə edin (`jdbc:postgresql://postgres:5432/...`) |
| `Could not resolve placeholder 'META_APP_PAGE_ID'` | `.env`/`application.yml`-dəki dəyişən adı kodda istifadə olunan `@Value` adı ilə uyğun gəlmir | Ad uyğunluğunu yoxlayın (`META_PAGE_ID` vs `META_APP_PAGE_ID`) |
| `POSTGRES_PASSWORD variable is not set` xəbərdarlığı | `.env` faylı `docker-compose.yaml` ilə eyni qovluqda deyil | `.env`-i compose faylı ilə yanaşı yerləşdirin, sonra `docker compose up` çağırın |
| `service refers to undefined volume` | `volumes:` bloku fayldan düşüb / yarımçıq köçürülüb | Faylın sonunda top-level `volumes:` bölməsinin tam olduğunu yoxlayın |

## 📌 Gələcək Təkmilləşdirmələr (Roadmap)

- [ ] Pagination üçün Meta Graph API-dən `paging.next` istifadəsi (hazırda sabit `limit` ilə işləyir)
- [ ] Planlaşdırılmış (scheduled) avtomatik sync — `@Scheduled` ilə manual `POST` çağırışına ehtiyacı aradan qaldırmaq
- [ ] Test əhatəliliyinin artırılması (unit + integration testlər)
- [ ] Swagger/OpenAPI sənədləşdirməsi

## 📄 Lisenziya

Bu layihə şəxsi/təhsil məqsədli inkişaf üçündür. Lisenziya növünü öz seçiminizə görə əlavə edin (MIT, Apache 2.0 və s.).
