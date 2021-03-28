from flask_wtf import FlaskForm
from flask_wtf.file import FileField, FileAllowed
from wtforms import StringField, PasswordField, SubmitField, BooleanField, TextAreaField, IntegerField
from wtforms.validators import Length, DataRequired, EqualTo, Email, ValidationError


class TransactionForm(FlaskForm):
    receiver = StringField('Receiver address', [DataRequired()])
    amt = IntegerField('Amount', [DataRequired()])
    submit = SubmitField('Send')

    # def validate_amt(self, amt):
    #     try:
    #         int(amt)
    #         if amt < 0:
    #             raise Exception('Negative amount not allowed')
    #     except Exception as e:
    #         raise Exception('Enter Integer Amount')
        

class MineForm(FlaskForm):
    submit = SubmitField('Mine')
