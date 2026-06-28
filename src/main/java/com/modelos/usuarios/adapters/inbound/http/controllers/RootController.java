package com.modelos.usuarios.adapters.inbound.http.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @Autowired(required = false)
    private BuildProperties buildProperties;

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String root() {
        String version = buildProperties != null ? buildProperties.getVersion() : "0.1.0";

        return """
                <!DOCTYPE html>
                <html lang="pt-BR">
                <head>
                  <meta charset="UTF-8" />
                  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
                  <title>API Details</title>
                  <style>
                    *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }
                    body {
                      font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
                      background: #f5f5f5;
                      display: flex;
                      justify-content: center;
                      align-items: center;
                      min-height: 100vh;
                    }
                    .card {
                      background: #fff;
                      border-radius: 12px;
                      padding: 2rem 2.5rem;
                      box-shadow: 0 4px 24px rgba(0,0,0,.08);
                      min-width: 320px;
                    }
                    h1 { font-size: 1.4rem; color: #1a1a1a; margin-bottom: .25rem; }
                    .version { font-size: .8rem; color: #888; margin-bottom: 1.5rem; }
                    .section { margin-bottom: 1.25rem; }
                    .section-title {
                      font-size: .7rem;
                      font-weight: 600;
                      text-transform: uppercase;
                      letter-spacing: .06em;
                      color: #999;
                      margin-bottom: .5rem;
                    }
                    ul { list-style: none; }
                    li { margin-bottom: .35rem; }
                    a {
                      display: inline-flex;
                      align-items: center;
                      gap: .4rem;
                      color: #2563eb;
                      text-decoration: none;
                      font-size: .9rem;
                    }
                    a:hover { text-decoration: underline; }
                    .badge {
                      font-size: .65rem;
                      background: #e0edff;
                      color: #1d4ed8;
                      border-radius: 4px;
                      padding: 1px 5px;
                      font-weight: 600;
                    }
                  </style>
                </head>
                <body>
                  <div class="card">
                    <h1>Usuarios API Java</h1>
                    <p class="version">v%s</p>

                    <div class="section">
                      <div class="section-title">Documentação</div>
                      <ul>
                        <li><a href="/scalar">Scalar UI <span class="badge">interactive</span></a></li>
                        <li><a href="/docs">Swagger UI</a></li>
                        <li><a href="/openapi">OpenAPI Spec <span class="badge">JSON</span></a></li>
                      </ul>
                    </div>

                    <div class="section">
                      <div class="section-title">Health Probes</div>
                      <ul>
                        <li><a href="/health/live">GET /health/live</a></li>
                        <li><a href="/health/ready">GET /health/ready</a></li>
                      </ul>
                    </div>
                  </div>
                </body>
                </html>
                """.formatted(version);
    }

    @GetMapping(value = "/scalar", produces = MediaType.TEXT_HTML_VALUE)
    public String scalar() {
        return """
                <!DOCTYPE html>
                <html lang="pt-BR">
                <head>
                  <title>Usuarios API Java - Scalar</title>
                  <meta charset="utf-8" />
                  <meta name="viewport" content="width=device-width, initial-scale=1" />
                </head>
                <body>
                  <script
                    id="api-reference"
                    data-url="/openapi"
                  ></script>
                  <script src="https://cdn.jsdelivr.net/npm/@scalar/api-reference"></script>
                </body>
                </html>
                """;
    }
}
