---
title : GMM with flask
version : 0.0.1
writer : khosungpil
type : GMM with flask test document
local : KT cloud(ubuntu 16.04)
objective : GMM test
---

## Usage ##
pytorch = 1.0.1

torchvision = 0.2.1

tensorboardX = 1.7

~~~
└── gmm_flask_version
    ├── model
       └── men_tshirts
           └── GMM
               └── gmm_train_new
                   ├── gmm_final.pth    
                   ├── step_xxxxxx.pth
                   └── ...
    ├── testdata
        ├── cloth
        ├── cloth-mask
        ├── image
        ├── image-parse
        └── pose

    └── networktest
        ├── flask_server_to_deep.py
        └── pose_test.py
~~~

### Pre-trained ###
<a href="https://drive.google.com/file/d/18DJQHqWoq9my6mN_ocyyGrtNyVm6FNNL/view?usp=sharing">[GMM(men_tshirts)]</a>

### Test ###
~~~
<networktest>
python flask_server_to_deep.py
~~~
~~~
python test.py
~~~
~~~
<networktest>
python post_test.py
~~~