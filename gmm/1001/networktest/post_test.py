import urllib.request

data = urllib.parse.urlencode({
    'userid': 'test',
    'imageid' : '000003',
    'upperid': '102001',
    'lowerid' : '000000',
    'isupper' : '1', 
    'category': '1001'

})

url = "http://127.0.0.1:10008/submit"

data = data.encode('ascii')
with urllib.request.urlopen(url, data) as f:
    print(f.read().decode('utf-8'))
