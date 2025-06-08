# HTTP 서버 (GET/POST 처리, 계산 기능)

from http.server import BaseHTTPRequestHandler, HTTPServer
# BaseHTTPRequestHandler: HTTP 요청 처리하는 기본클래스
# HTTPServer: 실제 서버 실행하는 클래스

class MyHttpHandler(BaseHTTPRequestHandler):

    # 들어온 HTTP 요청의 상세정보 출력 함수
    def print_http_request_detail(self):
        """Print HTTP request in detail."""
        print('::Client address   : {0}'.format(self.client_address[0]))
        print('::Client port      : {0}'.format(self.client_address[1]))
        print('::Request command  : {0}'.format(self.command))
        print('::Request line     : {0}'.format(self.requestline))
        print('::Request path     : {0}'.format(self.path))
        print('::Request version  : {0}'.format(self.request_version))

    # HTTP 응답 헤더를 생성함: 브라우저에게 "요청 성공했고, HTML로 응답할게"
    def send_http_response_header(self):
        """Create and send http Response."""
        self.send_response(200)
        self.send_header("Content-type", "text/html")   # HTML 형태로 응답함
        self.end_headers()

    # GET 요청을 처리하는 메인 로직
    def do_GET(self):
        """HTTP GET request handler."""
        print("## do_GET() activated.")

        # GET response header generation
        self.print_http_request_detail()    # 요청 정보를 출력하고
        self.send_http_response_header()    # 응답헤더 전송

        # GET request for multiplication (parameter transfer through Get request)
        # ex: http://localhost:8080/?var1=9&var2=9

        if '?' in self.path:    # http://localhost:8080/?var1=9&var2=9 형태의 URL
            # parameter retrieval and calculation
            routine = self.path.split('?')[1]   # "var1=9&var2=9"
            parameter = self.parameter_retrieval(routine)   # [9, 9] 추출
            result = self.simple_calc(int(parameter[0]), int(parameter[1])) # 9 * 9 계산

            # GET response generation
            # HTML 응답 생성하기
            self.wfile.write(bytes("<html>", "utf-8"))  # 문자열을 바이트로 변환해줘야 함. HTTP 프로토콜 자체가 바이트 기반임.
            # 파이썬은 이렇게 직접 바이트 변환을 해줘야 하는데, 장고/플라스크
            get_response = "GET request for calculation => {0} x {1} = {2}".format(parameter[0], parameter[1], result)
            self.wfile.write(bytes(get_response, "utf-8")) 
            self.wfile.write(bytes("</html>", "utf-8"))
            print("## GET request for calculation => {0} x {1} = {2}.".format(parameter[0], parameter[1], result))

        # GET request for directory retrieval
        # ex: http://localhost:8080/directory_sub/
        else:           # 그냥 일반 경로 요청인 경우에
            # GET response generation
            # 단순히 경로만 표시하기
            self.wfile.write(bytes("<html>", "utf-8"))
            self.wfile.write(bytes("<p>HTTP Request GET for Path: %s</p>" % self.path, "utf-8"))
            self.wfile.write(bytes("</html>", "utf-8"))
            print("## GET request for directory => {0}.".format(self.path))

    def do_POST(self):
        """HTTP POST request handler."""
        print("## do_POST() activated.")

        # GET response header generation
        self.print_http_request_detail()
        self.send_http_response_header()

        # parameter retrieval and calculation

        # POST 데이터 읽기
        content_length = int(self.headers['Content-Length']) # <--- Gets the size of data       # 데이터 크기
        post_data = self.rfile.read(content_length) # <--- Gets the data itself                 # 실제 데이터를 읽기
        parameter = self.parameter_retrieval(post_data.decode('utf-8'))                         # 파라미터 추출
        result = self.simple_calc(int(parameter[0]), int(parameter[1]))                         # 계산하기
        
        # POST response generation
        # 응답 전송
        post_response = "POST request for calculation => {0} x {1} = {2}".format(parameter[0], parameter[1], result)
        self.wfile.write(bytes(post_response, "utf-8")) 
        print("## POST request data => {0}.".format(post_data.decode('utf-8')))   
        print("## POST request for calculation => {0} x {1} = {2}.".format(parameter[0], parameter[1], result))   

    # 유틸리티 함수들
    def log_message(self, format, *args):
        """Turn off default http.server log message."""
        return  # 기본 로그 메시지는 끄기

    def simple_calc(self, para1, para2):
        """Multiplication function."""
        return para1 * para2       # 곱셈 계산하는 함수

    def parameter_retrieval(self, msg): # "var1=9&var2=9" -> [9, 9] 변환하도록
        """Parameter retrieval function for multiplication."""
        result = []
        fields = msg.split('&')     # ['var1=9', 'var2=9']
        result.append(int(fields[0].split('=')[1])) # 9
        result.append(int(fields[1].split('=')[1])) # 9
        return result

# 서버 실행부
if __name__ == "__main__": 
    """Main function."""
    server_name = "localhost"
    server_port = 8080

    webServer = HTTPServer((server_name, server_port), MyHttpHandler)   # 서버를 생성
    print("## HTTP server started at http://{0}:{1}.".format(server_name, server_port))

    try:
        webServer.serve_forever()   # 서버를 계속 실행
    except KeyboardInterrupt:       # Ctrl+C로 종료
        pass

    webServer.server_close()        # 서버 종료
    print("HTTP server stopped.")