# 📋 Project Management System

![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-brightgreen?style=flat-square&logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?style=flat-square&logo=postgresql)
![JWT](https://img.shields.io/badge/JWT-Auth-purple?style=flat-square&logo=jsonwebtokens)

> **Project Management System** — loyihalarni boshqarish uchun mo'ljallangan production darajadagi REST API.  
> Rol asosidagi kirish huquqi, JWT autentifikatsiya va real vaqt dashboard imkoniyatlarini o'z ichiga oladi.

---

## 🎯 Loyihaning Maqsadi

Bu tizim kompaniyalar va jamoalar uchun loyihalarni markazlashgan holda boshqarishga yordam beradi.
Har bir foydalanuvchi o'z roliga qarab tizimda turli huquqlarga ega bo'ladi.
Loyiha davomiyligini, daromadini va jarayon foizini kuzatib borish mumkin.

---

## 💡 Nima Qila Oladi?

### 👤 Foydalanuvchi (USER)
- Tizimga ro'yxatdan o'tish va kirish
- O'z loyihalarini yaratish, ko'rish, yangilash va o'chirish
- Dashboard orqali umumiy statistikani kuzatish

### 📌 Loyiha Menejeri (PM)
- O'ziga biriktirilgan loyihalarni ko'rish va yangilash
- Dashboard orqali statistikani kuzatish

### 👑 Administrator (ADMIN)
- Barcha loyihalarni ko'rish va o'chirish
- Foydalanuvchilarga rol tayinlash
- Loyihalarga PM biriktirish
- Yillik KPI maqsadini belgilash
- Dashboard orqali to'liq tahlil

---

## 🛠 Texnologiyalar

| Kategoriya | Texnologiya |
|------------|-------------|
| Dasturlash tili | Java 17 |
| Framework | Spring Boot 3.x |
| Xavfsizlik | Spring Security + JWT |
| Ma'lumotlar bazasi | PostgreSQL |
| ORM | Spring Data JPA / Hibernate |
| API hujjatlash | Swagger (SpringDoc OpenAPI) |
| Build Tool | Maven |

---

## 🌐 API Endpointlar

### 🔐 Autentifikatsiya
| Method | Endpoint | Kirish | Tavsif |
|--------|----------|--------|--------|
| `POST` | `/api/auth/register` | Hammaga | Yangi foydalanuvchi ro'yxatdan o'tish |
| `POST` | `/api/auth/login` | Hammaga | Tizimga kirish va JWT token olish |
| `PATCH` | `/api/auth/users/{id}/role` | ADMIN | Foydalanuvchiga rol tayinlash |

### 📁 Loyihalar
| Method | Endpoint | Kirish | Tavsif |
|--------|----------|--------|--------|
| `POST` | `/api/projects/create` | Hammaga | Yangi loyiha yaratish |
| `GET` | `/api/projects` | Hammaga | Loyihalar ro'yxati (rol bo'yicha) |
| `GET` | `/api/projects/project/{id}` | Hammaga | Loyihani ID bo'yicha olish |
| `PUT` | `/api/projects/update/{id}` | USER, PM | Loyihani yangilash |
| `DELETE` | `/api/projects/delete/{id}` | ADMIN, USER | Loyihani o'chirish |
| `PATCH` | `/api/projects/{id}/assign-pm` | ADMIN | Loyihaga PM biriktirish |

### 📊 Dashboard
| Method | Endpoint | Kirish | Tavsif |
|--------|----------|--------|--------|
| `GET` | `/api/dashboard?year=2025` | Hammaga | To'liq dashboard ma'lumotlari |
| `PATCH` | `/api/dashboard/kpi` | ADMIN | Yillik KPI maqsadini belgilash |

---

## 📊 Dashboard Imkoniyatlari

| Widget | Tavsif |
|--------|--------|
| 📈 Oylik daromad | Tanlangan yil bo'yicha 12 oylik daromad grafigi |
| 🍩 Biznes turi | Loyiha turi (SI, SM...) bo'yicha daromad foizi, top 4 loyiha |
| 🎯 KPI | Maqsad va haqiqiy daromad taqqoslash, bajarilish foizi |
| ⚠️ Kechikkan PM | Eng ko'p kechikkan loyihalarni boshqaruvchi PMlar, top 5 |

---

## 🔒 Xavfsizlik

| Status | Holat |
|--------|-------|
| `401 UNAUTHORIZED` | Token yo'q yoki yaroqsiz |
| `403 ACCESS_DENIED` | Huquq yetarli emas |
| `404 NOT_FOUND` | Resurs topilmadi |
| `400 VALIDATION_ERROR` | Noto'g'ri ma'lumot kiritildi |

---

## 📖 API Hujjatlari

Ilova ishga tushgandan so'ng Swagger UI orqali barcha endpointlarni ko'rish mumkin:

```
http://localhost:8080/swagger-ui/index.html
```

---

<div align="center">
  Spring Boot yordamida ❤️ bilan yaratildi
</div>
