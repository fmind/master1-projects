# -*- coding: utf-8 -*-

class DatabaseRouter(object):
    ''' Route model to a database '''
    def db_for_read(self, model, **hints):
        if hasattr(model, 'DATABASE'):
            return model.DATABASE
        return None

    def db_for_write(self, model, **hints):
        if hasattr(model, 'DATABASE'):
            return model.DATABASE
        return None

    def allow_relation(self, obj1, obj2, **hints):
        return True

    def allow_syncdb(self, db, model):
        model_db = model.DATABASE if hasattr(model, 'DATABASE') else 'default'

        if model_db == db:
            return True
        return False

