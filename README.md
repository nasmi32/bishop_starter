В стартере реализовано:
1. Модуль приема и исполнения команд - CommandService (в зависимости от приоритета распределяет на выполнение);
2. Возможность мониторинга текущей занятости андроида - MetricsService - рассматривается текущая длина очереди http://localhost:8080/actuator/metrics/command.queue.size, а также все авторы http://localhost:8080/actuator/metrics/commands.processed и для каждого автора личное число команд http://localhost:8080/actuator/metrics/commands.processed?tag=author:AUTHORS_NAME;
3. Возможность аудита любых действий андроида -  - @WeylandWatchingYou и AuditAspect. Также реализовано оба варианта - данные отправляются в кафку, либо в консоль (в прототипе указывается в application.properties);
4. Модуль обработки ошибок - GlobalExceptionHandler.