# FLUJO_DE_RECOMENDACION

Guía de colaboración para el repositorio. Cada integrante trabaja en la rama de su issue y publica su trabajo vía Pull Request hacia `develop`.

---

## FLUJO: ¿Qué hago cuando termino mi issue?

### 1) Antes de subir nada — verificación local

```bash
# Revisa qué cambiaste (nombres de archivos, no contenido)
git status

# Mira el resumen de tu último cambio (debe ser 1 sola tarea)
git diff --stat

# Confirma que estás en la rama correcta del issue
git branch --show-current
#  Debe mostrar: feat/<tu-alias>/<tarea>
```

> Regla de oro: **1 rama = 1 issue**. Si tu cambio mezcla 2 tareas, sepáralo.

### 2) Commit de tu trabajo

```bash
git add .                                          # Agrega todo lo modificado
git commit -m "feat(scope): qué hace tu código"    # Mensaje tipo: feat(auth): implementa register en AuthService
```

### 3) Subir tu rama al repo compartido

```bash
git push origin feat/<tu-alias>/<tarea>
```

### 4) Crear el Pull Request HACIA develop (NUNCA main)

```bash
gh pr create \
  --base develop \
  --head feat/<tu-alias>/<tarea> \
  --title "feat(auth): register en AuthService" \
  --body "Closes #<número-de-tu-issue>"
```

> `Closes #N` es clave: al mergear, GitHub cierra el issue automáticamente.

### 5) Esperar la revisión de tu compañero

- El repo exige 1 aprobación para mergear.
- Tú no mergeas tu propio PR — otro debe dar el ✅.
- Si te piden cambios: edita en la misma rama, vuelve a `git push`, y se actualiza el PR solo.

### 6) Cerrar tu PR cuando esté aprobado

```bash
gh pr merge <número> --merge
```

### 7) Al final del día (o al día siguiente) — sincronizarte

```bash
git fetch origin                            # Descarga lo nuevo del repo
git checkout develop                        # Pásate a la rama base
git pull origin develop                     # Trae los PRs de los demás
```

---

## Recomendaciones generales

**A) Actualizar `develop` ANTES de empezar el siguiente issue**

```bash
git pull origin develop
git checkout -b feat/<tu-alias>/<tarea2>    # Nace desde develop fresca
```

Evita conflictos porque todos parten de la misma base.

**B) Commits pequeños y descriptivos** (no uno gigante al final):

```bash
git commit -m "feat(auth): agrega entidad User con campos base"
git commit -m "test(auth): cubre register con datos válidos"
```

**C) Antes de pushear, integrar los cambios del resto para evitar choques:**

```bash
git fetch origin
git rebase origin/develop                   # Acomoda tu trabajo encima de lo nuevo
```

> Si hay conflictos se resuelven en tu rama, antes del PR.

**D) Actualizar el issue al crear el PR:** agrega un comentario en el issue: *"Listo, PR #XX — requiere revisión"*. Así el tablero comunica sin preguntar.

**E) No cerrar milestones tú mismo:** márcate el hito de referencia, pero el cierre de M0–M5 lo coordina el equipo al final de cada fase.

---

## Qué esperar como colaborador (general)

| Situación | Qué esperas |
|---|---|
| Creas tu rama y trabajas | Tuya hasta el PR; nadie la toca |
| Abres el PR | Alguien te da 1 ✅ obligatorio o te pide cambios en 1–2 días |
| Te piden cambios | Los haces en tu rama y vuelves a pushear; el PR se actualiza solo |
| Tu PR se mergea | El issue se cierra solo (`Closes #N`) |
| Ver lo de los demás | `git pull origin develop` cuando estés en `develop` |
| Banner "Compare & pull request" | IGNORAR — normal, `develop` va adelante; a `main` solo se mergea al cerrar hitos |
| Duda sobre técnica | Pregunta en el issue con `@<compañero>` (queda registro) |

### Reglas del repo

- PRs siempre hacia `develop`, nunca a `main`.
- Nadie se auto-aprueba ni mergea sin revisión.
- Las ramas `feat/*` ya existen y no se crean nuevas (usa la tuya).
- Si la rama de un issue no es tuya, detente: es de otro, coordina antes de tocar.