import urllib.request

data = urllib.parse.urlencode({
    'userid': 'sungpil',
    'imageid': '000001',
    'upperid': '000000',
    'lowerid': '000012',
    'isupper': '0',
    'category': '1101'

})

url = "http://127.0.0.1:10009/submit"

data = data.encode('ascii')
with urllib.request.urlopen(url, data) as f:
    print(f.read().decode('utf-8'))
