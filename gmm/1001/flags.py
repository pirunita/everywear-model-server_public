import argparse

def get_flags():
    parser = argparse.ArgumentParser()
    parser.add_argument("--user_id", type=str, default='', help='User ID')
    parser.add_argument("--user_image_id", type=str, default='', help='User picture ID')
    parser.add_argument("--upper_id", type=str, default='', help='Composed upper ID which is selected')
    parser.add_argument("--lower_id", type=str, default='', help='Composed lower ID which is selected')
    parser.add_argument("--is_upper", type=int, default=0, help='0: no composed upper, 1: composed upper')
    parser.add_argument("--category_num", type=str, default='1001', help='categorized number')
    parser.add_argument("--category_name", type=str, default='', help='category name based c_num')

    parser.add_argument("--input_dir", type=str, default='', help='user input directory')
    parser.add_argument("--stage1_dir", type=str, default='', help='user stage(gmm,edm) directory')
    parser.add_argument("--stage2_dir", type=str, default='', help='user stage(edm,rm) directory')
    parser.add_argument("--output_dir", type=str, default='', help='user output directory')

    parser.add_argument("--product_dir", type=str, default='', help='cloth input directory')

    parser.add_argument("--middle_composed_name", type=str, default='', help='userimage_prodimage_')
    parser.add_argument("--full_composed_name", type=str, default='', help='userimage_upper_lower_')
    flags = parser.parse_args()
    return flags
