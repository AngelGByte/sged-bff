try {
  $baseUsers = 'http://localhost:8081'
  $baseNotas = 'http://localhost:8082'
  $baseCursos = 'http://localhost:8083'
  $baseBff = 'http://localhost:8080'

  Write-Output 'POST /api/usuarios -> crear admin'
  $admin = @{nombre='Admin'; apellido='Colegio'; email='admin@colegio.cl'; password='admin123'; rol='ADMINISTRADOR'} | ConvertTo-Json
  try {
    $respAdmin = Invoke-WebRequest -Uri "$baseUsers/api/usuarios" -Method Post -Body $admin -ContentType 'application/json'
    Write-Output "Admin creado: $($respAdmin.Content | ConvertFrom-Json | ConvertTo-Json -Depth 3)"
  } catch { Write-Output "Advertencia: no se pudo crear admin: $($_.Exception.Message)" }

  Write-Output 'POST /api/auth/login -> obtener token'
  $login = @{email='admin@colegio.cl'; password='admin123'} | ConvertTo-Json
  $loginResp = Invoke-WebRequest -Uri "$baseUsers/api/auth/login" -Method Post -Body $login -ContentType 'application/json'
  $loginData = $loginResp.Content | ConvertFrom-Json
  Write-Output "Login response: $($loginData | ConvertTo-Json -Depth 3)"
  if ($loginData.token) { $token = $loginData.token } elseif ($loginData.accessToken) { $token = $loginData.accessToken } elseif ($loginData.data -and $loginData.data.token) { $token = $loginData.data.token } else { $token = $loginData | Select-String -Pattern 'eyJ' -AllMatches | ForEach-Object { $_.Matches.Value } | Select-Object -First 1 }
  if (-not $token) { throw 'No se pudo obtener token de login' }
  Write-Output "Token obtenido: $($token.Substring(0,20) + '...')"
  $authHeader = @{ Authorization = "Bearer $token"; 'Content-Type' = 'application/json' }

  $timestamp = (Get-Date).ToString('yyyyMMddHHmmss')
  $docEmail = "docente-$timestamp@colegio.cl"
  $cursoNombre = "1A-$timestamp"

  Write-Output 'POST /api/usuarios -> crear docente'
  $doc = @{nombre='Maria'; apellido='Gonzalez'; email=$docEmail; password='doc123'; rol='DOCENTE'} | ConvertTo-Json
  try {
    $respDoc = Invoke-WebRequest -Uri "$baseUsers/api/usuarios" -Method Post -Body $doc -Headers $authHeader -ContentType 'application/json' -ErrorAction Stop
    $docData = $respDoc.Content | ConvertFrom-Json
    Write-Output "Docente creado: $($docData | ConvertTo-Json -Depth 3)"
  } catch { Write-Output "Advertencia: no se pudo crear docente: $($_.Exception.Message)" }

  Write-Output 'POST /api/cursos -> crear curso'
  $curso = @{nombre=$cursoNombre; nivel='1'; letra='A'; docenteJefeId=2; anio=2024} | ConvertTo-Json
  try {
    $respCurso = Invoke-WebRequest -Uri "$baseCursos/api/cursos" -Method Post -Body $curso -Headers $authHeader -ContentType 'application/json' -ErrorAction Stop
    $cursoData = $respCurso.Content | ConvertFrom-Json
    Write-Output "Curso creado: $($cursoData | ConvertTo-Json -Depth 3)"
  } catch { Write-Output "Advertencia: no se pudo crear curso: $($_.Exception.Message)" }

  Write-Output 'POST /api/notas -> registrar nota'
  $nota = @{estudianteId=1; docenteId=2; cursoId=1; asignatura='Matematicas'; tipoEvaluacion='Prueba'; calificacion=6.5; fecha='2024-05-15'; semestre='PRIMERO'; anio=2024} | ConvertTo-Json
  try {
    $respNota = Invoke-RestMethod -Uri "$baseNotas/api/notas" -Method Post -Body $nota -Headers $authHeader -ContentType 'application/json' -ErrorAction Stop
    Write-Output "Nota creada: $($respNota | ConvertTo-Json -Depth 3)"
  } catch { Write-Output "Advertencia: no se pudo crear nota: $($_.Exception.Message)" }

  Write-Output 'GET BFF dashboard -> /api/bff/dashboard/estudiante/1'
  try {
    $dashboard = Invoke-RestMethod -Uri "$baseBff/api/bff/dashboard/estudiante/1" -Method Get -Headers $authHeader -ErrorAction Stop
    Write-Output "Dashboard: $($dashboard | ConvertTo-Json -Depth 5)"
  } catch { Write-Output "Advertencia: no se pudo obtener dashboard: $($_.Exception.Message)" }

  Write-Output 'OK'
} catch {
  Write-Output "ERROR: $($_.Exception.Message)"
  if ($_.Exception.Response) { try { $content = $_.Exception.Response.GetResponseStream() | Select-Object -First 1; Write-Output $content } catch {} }
  exit 1
}
